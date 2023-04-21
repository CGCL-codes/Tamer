package jenes.problems.menulayout;

import java.util.ArrayList;
import java.util.List;

public class Data {

    public static class GenerationData {

        private List<Double> generationdata;

        private long time;

        public GenerationData() {
            this.generationdata = new ArrayList<Double>();
        }

        public List<Double> getGenerationData() {
            return generationdata;
        }

        public int length() {
            return generationdata == null ? -1 : generationdata.size();
        }

        public void add(Double d) {
            this.generationdata.add(d);
        }

        public void addTime(long t) {
            this.time = t;
        }

        public long getTime() {
            return time;
        }
    }

    public GenerationData[] array;

    public Data(int length) {
        array = new GenerationData[length];
    }

    public void add_inner(int pos, GenerationData l) {
        this.array[pos] = l;
    }
}
