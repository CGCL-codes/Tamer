package jofc2.model.elements;

import jofc2.model.metadata.Alias;

public class SketchBarChart extends FilledBarChart {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7562070898232847510L;

    private static final transient String TYPE = "bar_sketch";

    @Alias("offset")
    private Integer funFactor;

    public SketchBarChart() {
        super(TYPE);
    }

    public SketchBarChart(String colour, String outlineColour, Integer funFactor) {
        super(TYPE);
        setColour(colour);
        setOutlineColour(outlineColour);
        setFunFactor(funFactor);
    }

    public Integer getFunFactor() {
        return funFactor;
    }

    public BarChart setFunFactor(Integer funFactor) {
        this.funFactor = funFactor;
        return this;
    }

    public static class Bar extends FilledBarChart.Bar {

        @Alias("offset")
        private Integer funFactor;

        public Bar(Number top) {
            super(top);
        }

        public Bar(Number top, Integer funFactor) {
            super(top);
            setFunFactor(funFactor);
        }

        public Bar(Number top, Number bottom, Integer funFactor) {
            super(top, bottom);
            setFunFactor(funFactor);
        }

        public Bar setFunFactor(Integer funFactor) {
            this.funFactor = funFactor;
            return this;
        }

        public Integer getFunFactor() {
            return funFactor;
        }
    }
}
