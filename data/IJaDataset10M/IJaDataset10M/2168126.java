package moller.javapeg.program.datatype;

import moller.util.datatype.Rational;
import moller.util.string.StringUtil;

public class ExposureTime implements Comparable<ExposureTime> {

    private int seconds;

    private Rational partsOfSecond;

    private float exposureTimeAsFloat;

    public ExposureTime(String exposureTime) throws ExposureTimeException {
        exposureTime = StringUtil.removeAnyPrecedingAndTrailingNonIntegerCharacters(exposureTime);
        if (exposureTime.contains(Rational.DELIMITER)) {
            exposureTime = exposureTime.trim();
            int delimiterIndex = -1;
            if (exposureTime.contains(" ")) {
                String[] parts = exposureTime.split(" ");
                if (parts.length != 2) {
                    throw new ExposureTimeException("Invalid format of ExposureTime: " + exposureTime);
                }
                String seconds = parts[0];
                String partsOfSeconds = parts[1];
                delimiterIndex = partsOfSeconds.indexOf(Rational.DELIMITER);
                this.seconds = Integer.parseInt(seconds);
                this.partsOfSecond = new Rational(partsOfSeconds.substring(0, delimiterIndex), partsOfSeconds.substring(delimiterIndex + 1, partsOfSeconds.length()));
                this.exposureTimeAsFloat = Float.MIN_VALUE;
            } else {
                delimiterIndex = exposureTime.indexOf(Rational.DELIMITER);
                this.seconds = -1;
                this.partsOfSecond = new Rational(exposureTime.substring(0, delimiterIndex), exposureTime.substring(delimiterIndex + 1, exposureTime.length()));
                this.exposureTimeAsFloat = Float.MIN_VALUE;
            }
        } else if (exposureTime.contains(".") || exposureTime.contains(",")) {
            if (exposureTime.contains(",")) {
                exposureTime = exposureTime.replace(",", ".");
            }
            try {
                this.seconds = -1;
                this.partsOfSecond = null;
                this.exposureTimeAsFloat = Float.parseFloat(exposureTime);
            } catch (NumberFormatException nfex) {
                throw new ExposureTimeException("Invalid format of ExposureTime: " + exposureTime);
            }
        } else {
            try {
                this.seconds = Integer.parseInt(exposureTime.trim());
                this.partsOfSecond = null;
                this.exposureTimeAsFloat = Float.MIN_VALUE;
            } catch (NumberFormatException nfex) {
                throw new ExposureTimeException("Invalid format of ExposureTime: " + exposureTime);
            }
        }
    }

    @Override
    public String toString() {
        if (partsOfSecond == null && seconds == -1 && exposureTimeAsFloat == Float.MIN_VALUE) {
            return "";
        } else if (exposureTimeAsFloat == Float.MIN_VALUE && partsOfSecond == null) {
            return Integer.toString(seconds);
        } else if (exposureTimeAsFloat == Float.MIN_VALUE && seconds == -1) {
            return partsOfSecond.toString();
        } else if (partsOfSecond == null && seconds == -1) {
            return Float.toString(exposureTimeAsFloat);
        } else {
            return Integer.toString(seconds) + " " + partsOfSecond.toString();
        }
    }

    private float comparableValue() {
        float result = 0;
        if (seconds > 0) {
            result += seconds;
        }
        if (partsOfSecond != null) {
            result += (float) partsOfSecond.getNumerator() / (float) partsOfSecond.getDenominator();
        }
        if (exposureTimeAsFloat != Float.MIN_NORMAL) {
            result += exposureTimeAsFloat;
        }
        return result;
    }

    public class ExposureTimeException extends Exception {

        private static final long serialVersionUID = 1L;

        ExposureTimeException(String message) {
            super(message);
        }
    }

    @Override
    public int compareTo(ExposureTime o) {
        if (this.comparableValue() < o.comparableValue()) {
            return -1;
        } else if (this.comparableValue() == o.comparableValue()) {
            return 0;
        } else {
            return 1;
        }
    }
}
