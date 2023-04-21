package com.linguamathematica.oa4j;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.doubleToLongBits;
import static java.lang.String.format;

/**
 * The Class Analysis.
 */
public class Analysis {

    private final StylesAnalysis styles;

    private final DemographicsAnalysis demographics;

    private final ActionsAnalysis actions;

    private final TopicsAnalysis topics;

    /**
	 * Instantiates a new analysis.
	 * 
	 * @param demographics
	 *            the demographics
	 * @param styles
	 *            the styles
	 * @param actions
	 *            the actions
	 * @param topics
	 *            the topics
	 */
    public Analysis(final DemographicsAnalysis demographics, final StylesAnalysis styles, final ActionsAnalysis actions, final TopicsAnalysis topics) {
        this.demographics = demographics;
        this.styles = styles;
        this.actions = actions;
        this.topics = topics;
    }

    /**
	 * Gets the actions analysis.
	 * 
	 * @return the actions analysis
	 */
    public ActionsAnalysis getActionsAnalysis() {
        return actions;
    }

    @Override
    public String toString() {
        return format("Analysis %n%n%s %n%s %n%s %n%n%s", topics, actions, demographics, styles);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + actions.hashCode();
        result = prime * result + demographics.hashCode();
        result = prime * result + styles.hashCode();
        result = prime * result + topics.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Analysis other = (Analysis) obj;
        return actions.equals(other.actions) && demographics.equals(other.demographics) && styles.equals(other.styles) && topics.equals(other.topics);
    }

    /**
	 * Gets the demographic analysis.
	 * 
	 * @return the demographic analysis
	 */
    public DemographicsAnalysis getDemographicAnalysis() {
        return demographics;
    }

    /**
	 * Gets the styles analysis.
	 * 
	 * @return the styles analysis
	 */
    public StylesAnalysis getStylesAnalysis() {
        return styles;
    }

    /**
	 * Gets the topics analysis.
	 * 
	 * @return the topics analysis
	 */
    public TopicsAnalysis getTopicsAnalysis() {
        return topics;
    }

    /**
	 * The Enum Decisiveness.
	 */
    public enum Decisiveness {

        /** The LOW. */
        LOW, /** The MEDIU m_ low. */
        MEDIUM_LOW, /** The MEDIUM. */
        MEDIUM, /** The MEDIU m_ high. */
        MEDIUM_HIGH, /** The HIGH. */
        HIGH, /** The NA. */
        NA
    }

    /**
	 * The Class NER.
	 */
    public static class NER {

        /** The type. */
        private final String type;

        /** The subtype. */
        private final String subtype;

        /** The Constant NULL. */
        public static final NER NULL = new NER("", "") {

            @Override
            public boolean equals(final Object obj) {
                return obj == this;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public String toString() {
                return "-";
            }
        };

        /**
		 * Instantiates a new nER.
		 * 
		 * @param type
		 *            the type
		 * @param subtype
		 *            the subtype
		 */
        public NER(final String type, final String subtype) {
            this.type = type;
            this.subtype = subtype;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final NER other = (NER) obj;
            return subtype.equals(other.subtype) && type.equals(other.type);
        }

        /**
		 * Gets the subtype.
		 * 
		 * @return the subtype
		 */
        public String getSubtype() {
            return subtype;
        }

        /**
		 * Gets the type.
		 * 
		 * @return the type
		 */
        public String getType() {
            return type;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + subtype.hashCode();
            return prime * result + type.hashCode();
        }

        @Override
        public String toString() {
            return format("[type: %s, subtype: %s]", type, subtype);
        }
    }

    /**
	 * The Enum OfferingGuidance.
	 */
    public enum OfferingGuidance {

        /** The NO t_ a t_ all. */
        NOT_AT_ALL, /** The T o_ som e_ extent. */
        TO_SOME_EXTENT, /** The A_ lot. */
        A_LOT
    }

    /**
	 * The Enum Polarity.
	 */
    public enum Polarity {

        /** The NEGATIVE. */
        NEGATIVE, /** The POSITIVE. */
        POSITIVE, /** The NEUTRAL. */
        NEUTRAL
    }

    /**
	 * The Class PolarityResult.
	 */
    public static class PolarityResult {

        /** The min. */
        private final Result<Polarity> min;

        /** The mean. */
        private final Result<Polarity> mean;

        /** The max. */
        private final Result<Polarity> max;

        /**
		 * Instantiates a new polarity result.
		 * 
		 * @param min
		 *            the min
		 * @param mean
		 *            the mean
		 * @param max
		 *            the max
		 */
        public PolarityResult(final Result<Polarity> min, final Result<Polarity> mean, final Result<Polarity> max) {
            this.min = min;
            this.mean = mean;
            this.max = max;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final PolarityResult other = (PolarityResult) obj;
            return max.equals(other.max) && mean.equals(other.mean) && min.equals(other.min);
        }

        /**
		 * Gets the max.
		 * 
		 * @return the max
		 */
        public Result<Polarity> getMax() {
            return max;
        }

        /**
		 * Gets the mean.
		 * 
		 * @return the mean
		 */
        public Result<Polarity> getMean() {
            return mean;
        }

        /**
		 * Gets the min.
		 * 
		 * @return the min
		 */
        public Result<Polarity> getMin() {
            return min;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + max.hashCode();
            result = prime * result + mean.hashCode();
            return prime * result + min.hashCode();
        }

        @Override
        public String toString() {
            return format("[min %s, mean %s, max %s]", min, mean, max);
        }
    }

    /**
	 * The Enum RequestingGuidance.
	 */
    public enum RequestingGuidance {

        /** The NO t_ a t_ all. */
        NOT_AT_ALL, /** The T o_ som e_ extent. */
        TO_SOME_EXTENT, /** The A_ lot. */
        A_LOT
    }

    /**
	 * The Class Result.
	 * 
	 * @param <V>
	 *            the value type
	 */
    public static class Result<V> {

        /** The value. */
        private final V value;

        /** The score. */
        private final double score;

        /** The Constant NULL_VALUE. */
        private static final Object NULL_VALUE = new Object();

        /** The Constant NULL. */
        @SuppressWarnings("rawtypes")
        private static final Result<?> NULL = new Analysis.Result() {

            @Override
            public boolean equals(final Object obj) {
                return this == obj;
            }

            @Override
            public Object getValue() {
                return NULL_VALUE;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public String toString() {
                return "";
            }
        };

        /**
		 * Instantiates a new result.
		 * 
		 * @param value
		 *            the value
		 * @param score
		 *            the score
		 */
        public Result(final V value, final double score) {
            this.value = value;
            this.score = score;
        }

        /**
		 * Instantiates a new result.
		 */
        private Result() {
            value = null;
            score = NEGATIVE_INFINITY;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Result<?> other = (Result<?>) obj;
            return doubleToLongBits(score) == doubleToLongBits(other.score) && value.equals(other.value);
        }

        /**
		 * Gets the score.
		 * 
		 * @return the score
		 */
        public double getScore() {
            return score;
        }

        /**
		 * Gets the value.
		 * 
		 * @return the value
		 */
        public V getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            final long temp = doubleToLongBits(score);
            result = prime * result + (int) (temp ^ temp >>> 32);
            return prime * result + value.hashCode();
        }

        @Override
        public String toString() {
            return format("[%s, %s]", value, score);
        }

        /**
		 * Null result.
		 * 
		 * @param <N>
		 *            the number type
		 * @return the result
		 */
        @SuppressWarnings("unchecked")
        public static <N> Result<N> nullResult() {
            return (Result<N>) NULL;
        }
    }

    /**
	 * The Enum Temporality.
	 */
    public enum Temporality {

        /** The PAST. */
        PAST, /** The RECEN t_ past. */
        RECENT_PAST, /** The PRESENT. */
        PRESENT, /** The FUTURE. */
        FUTURE, /** The NA. */
        NA
    }
}
