package javango.forms.widgets;

public class PercentInputWidget extends DecimalInputWidget {

    @Override
    protected String format(Double value) {
        return super.format(value * 100);
    }
}
