package skewreduce.seaflow;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skewreduce.framework.LoopingMapper;
import skewreduce.framework.MuxOutput;
import skewreduce.framework.WritableInputFormat;
import skewreduce.framework.WritableOutputFormat;
import skewreduce.framework.physical.PProcessOp;
import skewreduce.lib.Cube2;
import skewreduce.lib.KDTree;
import skewreduce.lib.KDTreePredicate;
import skewreduce.lib.NodeList;
import skewreduce.lib.Point3D;

public class LocalFoF extends PProcessOp {

    private static final Logger LOG = LoggerFactory.getLogger(LocalFoF.class);

    public static class ParticleInputFormat extends WritableInputFormat<LongWritable, Point3D> {

        private LongWritable key = new LongWritable();

        public LongWritable createKey() {
            return key;
        }

        public Point3D createValue() {
            return new Point3D();
        }

        public int getKeySize() {
            return 8;
        }

        public int getValueSize() {
            return 12;
        }

        @Override
        protected boolean isSplitable(JobContext context, Path fn) {
            return false;
        }
    }

    public static class MappingOutputFormat extends WritableOutputFormat<LongWritable, LongWritable> {
    }

    public static class MergeOutputFormat extends WritableOutputFormat<LongWritable, InternalParticle> {
    }

    static class Predicate implements KDTreePredicate<InternalParticle> {

        private InternalParticle seed;

        private final double radius;

        private final boolean opt;

        Predicate(double r, boolean opt) {
            radius = r;
            this.opt = opt;
        }

        public void set(InternalParticle seed) {
            this.seed = seed;
        }

        @Override
        public boolean evalSkip(InternalParticle o) {
            return (opt) ? o.canSkip() : false;
        }

        @Override
        public boolean evalMatch(InternalParticle o) {
            return seed != o && seed.distance(o) < radius;
        }

        @Override
        public boolean evalAdd(InternalParticle o) {
            if (o.isNotVisitedYet()) {
                o.pending();
                return true;
            }
            return false;
        }
    }

    public static class FoFMapper extends LoopingMapper<LongWritable, InternalParticle, LongWritable, LongWritable> {

        KDTree<InternalParticle> tree;

        Predicate pred;

        List<InternalParticle> q = new ArrayList<InternalParticle>();

        InternalParticle[] particles;

        float RADIUS;

        Cube2 cube;

        boolean randomizeInput;

        MuxOutput outputs;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            Configuration conf = context.getConfiguration();
            cube = new Cube2(conf.get(PARTITION_SPEC_ATTR));
            cube.print(System.out);
            RADIUS = conf.getFloat("dfof.params.eps", 0.00026042f);
            boolean opt = conf.getBoolean("dfof.params.kdtree.optimize", false);
            pred = new Predicate(RADIUS, opt);
            randomizeInput = conf.getBoolean("dfof.params.randomizeinput", false);
            outputs = new MuxOutput(context, "merge", 2);
        }

        private int expandCluster(InternalParticle seed) {
            long cid = seed.getID() + 1;
            seed.setCluster(cid);
            int sz = 1;
            InternalParticle p;
            while (!q.isEmpty()) {
                p = q.remove(q.size() - 1);
                if (!p.isVisited()) {
                    ++sz;
                    p.visit();
                    p.setCluster(cid);
                    pred.set(p);
                    tree.range(p, RADIUS, pred, q);
                }
            }
            return sz;
        }

        private void fof(Context context) {
            int found = 0;
            beginLoop(context, particles.length);
            for (InternalParticle p : particles) {
                if (!p.isVisited()) {
                    p.visit();
                    pred.set(p);
                    tree.range(p, RADIUS, pred, q);
                    expandCluster(p);
                    ++found;
                }
                incrLoop(context);
            }
            endLoop(context);
            LOG.info("{} clusters have been found", found);
        }

        private void toDisk(Context context) throws IOException, InterruptedException {
            LongWritable key = new LongWritable();
            LongWritable val = new LongWritable();
            beginLoop(context, particles.length);
            for (InternalParticle p : particles) {
                key.set(p.getID());
                val.set(p.getCluster());
                context.write(key, val);
                incrLoop(context);
            }
            endLoop(context);
        }

        private void toMergeState(Context context) throws IOException, InterruptedException {
        }

        private void toMergeOutput(Context context) throws IOException, InterruptedException {
            LongWritable key = new LongWritable();
            beginLoop(context, particles.length);
            for (InternalParticle p : particles) {
                if (cube.atSkin(p, RADIUS)) {
                    key.set(p.getID());
                    outputs.write(1, key, p);
                }
                incrLoop(context);
            }
            endLoop(context);
        }

        @Override
        public void run(Context context) throws IOException, InterruptedException {
            NodeList<InternalParticle> buffer = new NodeList<InternalParticle>();
            setup(context);
            beginLoop(context);
            while (context.nextKeyValue()) {
                buffer.add(new InternalParticle(context.getCurrentKey().get(), context.getCurrentValue()));
                incrLoop(context);
            }
            endLoop(context);
            particles = new InternalParticle[buffer.size()];
            buffer.toArray(particles);
            buffer = null;
            System.out.println("building KDtree...");
            tree = new KDTree<InternalParticle>(particles);
            System.out.println("building KDtree: DONE");
            if (randomizeInput) {
                List<InternalParticle> tmpL = Arrays.asList(particles);
                Collections.shuffle(tmpL);
            }
            fof(context);
            toDisk(context);
            toMergeState(context);
            toMergeOutput(context);
            cleanup(context);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
            if (outputs != null) outputs.close(context);
        }
    }

    protected Job createJob(Configuration conf) throws IOException {
        Job job = new Job(conf);
        job.setJarByClass(LocalFoF.class);
        job.setInputFormatClass(ParticleInputFormat.class);
        job.setOutputFormatClass(MappingOutputFormat.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(LongWritable.class);
        job.setMapperClass(FoFMapper.class);
        job.setNumReduceTasks(0);
        job.getConfiguration().setInt("skewreduce.monitoring.num.loops", 4);
        return job;
    }

    public static Job getJobInstance(Configuration conf) throws IOException {
        return new LocalFoF().createJob(conf);
    }
}
