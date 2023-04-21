package ch.unibe.inkml.util;

import ch.unibe.inkml.InkChannel;
import ch.unibe.inkml.InkMLComplianceException;
import ch.unibe.inkml.InkTracePoint;

public class NumberFormatter extends Formatter {

    public double explicit = 0;

    public boolean explicit_set = false;

    public double speed = 0;

    public boolean speed_set = false;

    public enum State {

        EXPLICIT, FIRST_O, SECOND_O
    }

    private State state = State.EXPLICIT;

    public NumberFormatter(InkChannel c) {
        super(c);
    }

    public String getNext(InkTracePoint sp) {
        if (getChannel().isIntermittent()) {
            return prepair(valueOf(sp.get(getChannel().getName())));
        } else {
            return getNext(sp.get(getChannel().getName()));
        }
    }

    public String getNext(double next) {
        if (state == State.EXPLICIT && !explicit_set) {
            explicit = next;
            explicit_set = true;
            return prepair(valueOf(next));
        } else if (state == State.EXPLICIT) {
            speed = next - explicit;
            speed_set = true;
            state = State.FIRST_O;
            explicit = next;
            explicit_set = true;
            return prepair("'" + valueOf(speed));
        } else {
            double v = (next - explicit) - speed;
            speed = next - explicit;
            speed_set = true;
            explicit = next;
            explicit_set = true;
            String mark = (state == State.FIRST_O) ? "\"" : "";
            state = State.SECOND_O;
            return prepair(mark + valueOf(v));
        }
    }

    private String prepair(String value) {
        switch(value.charAt(0)) {
            case '*':
            case '?':
            case '!':
            case '\'':
            case '"':
            case '-':
                return value;
            default:
                return " " + value;
        }
    }

    protected State getState() {
        return state;
    }

    protected void setState(State state) {
        this.state = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double consume(String result) throws InkMLComplianceException {
        switch(result.charAt(0)) {
            case '*':
                if (!hasLastValue()) {
                    throw new InkMLComplianceException("The value '*'  is not accepted at the first position.");
                }
                return getValueForPoint(getLastValue());
            case '?':
            case ',':
                if (!this.getChannel().isIntermittent()) {
                    throw new InkMLComplianceException("For non intermittent channel '" + this.getChannel().getName().toString() + "' its not acceptable to " + ((result.charAt(0) == ',') ? "drop the value." : "substitute the value with '?'."));
                }
                this.unsetLastValue();
                return Double.NaN;
            case '!':
                this.setState(NumberFormatter.State.EXPLICIT);
            case '\'':
                if (result.charAt(0) == '\'') {
                    this.setState(NumberFormatter.State.FIRST_O);
                }
            case '"':
                if (this.getChannel().isIntermittent()) {
                    throw new InkMLComplianceException("An intermittent does not accepts qualifier prefixes for the values");
                }
                if (result.charAt(0) == '"') {
                    if (this.getState() == NumberFormatter.State.EXPLICIT) {
                        throw new InkMLComplianceException("A numerical channel cannot switch from explicit values to second order values");
                    }
                    this.setState(NumberFormatter.State.SECOND_O);
                }
                result = result.substring(1);
            default:
                return getValueForPoint(Double.parseDouble(result));
        }
    }

    protected String valueOf(double d) {
        return NumberFormatter.printDouble(d);
    }

    public static String printDouble(double d) {
        if (Double.isNaN(d)) {
            return "?";
        }
        int straight = Math.abs((int) d);
        boolean minus = d < 0;
        int rest = (int) Math.round((Math.abs(d - (double) straight) * 1000));
        if (rest == 1000) {
            straight++;
            rest = 0;
        }
        if (rest == 0) {
            return String.valueOf(straight);
        }
        String result = "";
        boolean wrote = false;
        for (int i = 1; i <= 3; i++) {
            if (rest % Math.pow(10, i) != 0 || wrote) {
                result = String.valueOf(((int) ((rest % Math.pow(10, i)) / Math.pow(10, i - 1)))) + result;
                rest = rest - (int) (rest % Math.pow(10, i));
                wrote = true;
            }
        }
        if (!result.isEmpty()) {
            result = "." + result;
        }
        if (straight == 0) {
            return ((minus) ? "-" : "") + result;
        } else {
            return ((minus) ? "-" : "") + straight + result;
        }
    }

    private double getValueForPoint(double value) {
        switch(this.getState()) {
            case EXPLICIT:
                explicit = value;
                return explicit;
            case FIRST_O:
                speed = value;
                explicit = explicit + value;
                return explicit;
            case SECOND_O:
                speed = speed + value;
                explicit = explicit + speed;
                return explicit;
        }
        return Double.NaN;
    }
}
