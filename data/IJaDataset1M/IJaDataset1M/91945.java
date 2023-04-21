package pipes.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.lang.reflect.Type;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.sun.java.SwingWorker;
import evolve.Context;
import evolve.Darwin;
import evolve.GeneRandomizer;
import evolve.Genome;
import evolve.Mutation;
import evolve.Mutator;
import evolve.Population;
import evolve.Runner;
import evolve.Status;
import evolve.TypeBuilder;
import evolve.Chromasome.Phene;
import evolve.Evaluator.Evaluation;

public class EvolveProgress {

    private final class ProgressStatus implements Status {

        List<Integer> mTotals = new java.util.ArrayList<Integer>();

        List<Integer> mCurrents = new java.util.ArrayList<Integer>();

        List<String> mNames = new java.util.ArrayList<String>();

        {
            push();
        }

        public void notify(String message) {
            mDiagnostics.append(message + "\n");
        }

        java.util.Map<Type, Integer> mErrorCounts = new java.util.HashMap<Type, Integer>();

        public void onFail(Throwable ex, String context) {
            Class<?> exClass = ex.getClass();
            int prevCount = mErrorCounts.containsKey(exClass) ? mErrorCounts.get(exClass) : 0;
            mErrorCounts.put(exClass, prevCount + 1);
            String description = ex.getMessage();
            if (context != null) {
                System.out.print(context);
            }
            System.out.println(exClass.getSimpleName() + (description != null ? ": " + description : ""));
        }

        public void pop() {
            mCurrents.remove(mCurrents.size() - 1);
            mTotals.remove(mTotals.size() - 1);
            mNames.remove(mNames.size() - 1);
            updateProgress();
        }

        public void push(String name) {
            mNames.add(name);
            push();
            updateProgress();
        }

        private void push() {
            mCurrents.add(0);
            mTotals.add(1);
        }

        public void updateBest(Evaluation eval) {
            mBestGenome.setText("Score: " + eval.score + "\n" + expression(eval.genome));
        }

        public void updateProgress(int current, int total) {
            mCurrents.set(mCurrents.size() - 1, current);
            mTotals.set(mTotals.size() - 1, total);
            updateProgress();
        }

        private void updateProgress() {
            mProgress.setValue((int) (kProgressTicks * progress(0)));
            mProgressDetail.setValue((int) (kProgressTicks * detailProgress()));
            String detail = "";
            for (String name : mNames) {
                if (detail.length() > 0) {
                    detail += " : ";
                }
                detail += name;
            }
            mDetailLabel.setText(detail);
        }

        private double progress(int i) {
            double current = mCurrents.get(i);
            if (i < mCurrents.size() - 1) {
                current += progress(i + 1);
            }
            return current / mTotals.get(i);
        }

        private double detailProgress() {
            return progress(mCurrents.size() - 1);
        }

        public void currentPopulation(List<Evaluation> evaluated) {
            String text = "";
            int count = 5;
            try {
                for (Evaluation eval : evaluated) {
                    text += "Score = " + eval.score + "\n" + expression(eval.genome);
                    --count;
                    if (count == 0) {
                        return;
                    }
                    text += "\n---------------------------\n";
                }
            } finally {
                mTopFive.setText(text);
            }
        }
    }

    public interface Reciever {

        void recieve(Evaluation best);
    }

    private JDialog mDialog;

    private JProgressBar mProgress;

    private JProgressBar mProgressDetail;

    private JLabel mDetailLabel;

    private JTextArea mBestGenome;

    private JTextArea mTopFive;

    private JTextArea mDiagnostics;

    private Runner mRunner;

    private EvolveProbabilities mProbabilities;

    private final int kProgressTicks = 1000;

    private boolean mFailed = false;

    private boolean mAborted = false;

    private Darwin mDarwin;

    private String mPopPath;

    private Reciever mReciever;

    private Evaluation mBest;

    public EvolveProgress(Runner runner, Reciever reciever) {
        mRunner = runner;
        mReciever = reciever;
    }

    public void run(JDialog owner, final Integer populationSize, final String populationPath, final int generations, EvolveProbabilities probabilities, final Long seed) {
        mProbabilities = probabilities;
        mBest = null;
        mDialog = new JDialog(owner, "Evolve Progress");
        mDialog.setLocationByPlatform(true);
        mDialog.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        mDialog.setLayout(new BoxLayout(mDialog.getContentPane(), BoxLayout.Y_AXIS));
        mDialog.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent e) {
                abort();
            }
        });
        mProgress = new JProgressBar(0, kProgressTicks);
        mDialog.add(mProgress);
        mDetailLabel = new JLabel();
        mDetailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mDialog.add(mDetailLabel);
        mProgressDetail = new JProgressBar(0, kProgressTicks);
        mDialog.add(mProgressDetail);
        Font font = new Font("Monaco", Font.PLAIN, 12);
        mDiagnostics = createTextOutput(font, 150, "Diagnostics:");
        mBestGenome = createTextOutput(font, 200, "Best Genome:");
        mTopFive = createTextOutput(font, 500, "Current Top Five:");
        mDialog.pack();
        mDiagnostics.setText("seed = " + Long.toString(seed) + "L;\n");
        mDialog.setVisible(true);
        SwingWorker worker = new SwingWorker() {

            public Object construct() {
                mBest = evolve(populationSize, populationPath, generations, seed);
                mReciever.recieve(mBest);
                return mBest;
            }

            public void finished() {
                if (mAborted) {
                    mDiagnostics.append("Aborted.");
                } else if (mFailed) {
                    mDiagnostics.append("Failed.");
                } else {
                    if (mBest != null && mBest.score == mRunner.maxScore()) {
                        mDiagnostics.append("Succeeded.");
                    } else {
                        mDiagnostics.append("Done.");
                    }
                    mProgress.setValue(kProgressTicks);
                }
            }
        };
        worker.setName("EvolveProgressWorker");
        worker.start();
    }

    private JTextArea createTextOutput(Font font, int height, String title) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(font);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setPreferredSize(new Dimension(1000, height));
        panel.add(scroll);
        mDialog.add(panel);
        return textArea;
    }

    public String expression(Genome genome) {
        Context context = new Context(mRunner.registry());
        List<Phene> expressions = genome.express(context);
        StringBuilder result = new StringBuilder();
        for (Phene expression : expressions) {
            result.append(expression.name + " = ");
            result.append(expression.expression.toString());
            result.append('\n');
        }
        return result.toString();
    }

    public Evaluation evolve(int populationSize, String populationPath, int generations, long seed) {
        GeneRandomizer geneRandomizer = new GeneRandomizer(mProbabilities.getGeneProbabilities());
        TypeBuilder builder = new TypeBuilder(true, mProbabilities.getTypeProbabilities(), mRunner.typeConstraints());
        Status status = new ProgressStatus();
        Mutator mutator = new Mutator(new Mutation(mProbabilities.getMutationProbabilities(), builder, geneRandomizer));
        setDarwin(new Darwin(builder, mRunner, status, geneRandomizer, mutator, mProbabilities.getSurvivalRatios()));
        if (mPopPath != null) {
            mDarwin.setPopulationStorePath(mPopPath);
        }
        java.util.Random random = new java.util.Random(seed);
        try {
            Population initialPopulation;
            if (populationPath != null && populationPath.length() > 0) {
                initialPopulation = Population.load(populationPath);
                if (initialPopulation == null) {
                    mFailed = true;
                    return null;
                }
            } else {
                initialPopulation = mDarwin.initialPopulation(populationSize, random);
            }
            if (mDarwin.isStopped()) {
                return null;
            }
            return mDarwin.evolve(initialPopulation, generations, random);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            mFailed = true;
            return null;
        } finally {
            clearDarwin();
        }
    }

    private synchronized void setDarwin(Darwin darwin) {
        mDarwin = darwin;
    }

    private synchronized void clearDarwin() {
        if (mDarwin != null && mDarwin.isStopped()) {
            mAborted = true;
            mProgress.setValue(0);
            mProgressDetail.setValue(0);
        }
        mDarwin = null;
    }

    public synchronized void abort() {
        if (mDarwin != null) {
            mDarwin.abort();
        }
    }

    public void setPopulationStorePath(String path) {
        mPopPath = path;
    }
}
