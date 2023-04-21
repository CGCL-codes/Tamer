package automenta.netention.gwtdepr.ui.detail.property;

import java.util.List;
import automenta.netention.server.value.PropertyValue;
import automenta.netention.server.value.Unit;
import automenta.netention.server.value.Value;
import automenta.netention.server.value.real.RealBetween;
import automenta.netention.server.value.real.RealEquals;
import automenta.netention.server.value.real.RealIs;
import automenta.netention.server.value.real.RealLessThan;
import automenta.netention.server.value.real.RealMoreThan;
import automenta.netention.server.value.real.RealVar;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

public class RealPropertyPanel extends OptionPropertyPanel {

    private Label unitLabel;

    public RealPropertyPanel(String property) {
        super(property);
    }

    public RealPropertyPanel(String property, PropertyValue v) {
        super(property, v);
    }

    @Override
    protected void initOptions(List<PropertyOption> options) {
        options.add(new PropertyOption<RealIs>("is") {

            private TextBox isBox;

            @Override
            public Panel newEditPanel(RealIs v) {
                setValue(v);
                setIs();
                Panel p = new FlowPanel();
                isBox = new PropertyTextBox();
                isBox.setText(Double.toString(v.getValue()));
                p.add(isBox);
                return p;
            }

            @Override
            public RealIs widgetToValue(RealIs r) {
                r.setValue(Double.valueOf(isBox.getText()));
                return r;
            }

            @Override
            public boolean accepts(Value v) {
                return v.getClass().equals(RealIs.class);
            }

            @Override
            public RealIs newDefaultValue() {
                return new RealIs(0);
            }
        });
        options.add(new PropertyOption<RealEquals>("will equal") {

            private TextBox equalsBox;

            @Override
            public Panel newEditPanel(RealEquals v) {
                setValue(v);
                setWillBe();
                Panel p = new FlowPanel();
                equalsBox = new PropertyTextBox();
                equalsBox.setText(Double.toString(v.getValue()));
                p.add(equalsBox);
                return p;
            }

            @Override
            public boolean accepts(Value v) {
                return v.getClass().equals(RealEquals.class);
            }

            @Override
            public RealEquals widgetToValue(RealEquals r) {
                r.setValue(Double.valueOf(equalsBox.getText()));
                return r;
            }

            @Override
            public RealEquals newDefaultValue() {
                return new RealEquals(0);
            }
        });
        options.add(new PropertyOption<RealMoreThan>("will be greater than") {

            private TextBox moreThanBox;

            @Override
            public Panel newEditPanel(RealMoreThan v) {
                setValue(v);
                setWillBe();
                Panel p = new FlowPanel();
                moreThanBox = new PropertyTextBox();
                moreThanBox.setText(Double.toString(v.getValue()));
                p.add(moreThanBox);
                return p;
            }

            @Override
            public RealMoreThan widgetToValue(RealMoreThan r) {
                r.setValue(Double.valueOf(moreThanBox.getText()));
                return r;
            }

            @Override
            public boolean accepts(Value v) {
                return v.getClass().equals(RealMoreThan.class);
            }

            @Override
            public RealMoreThan newDefaultValue() {
                return new RealMoreThan(0);
            }
        });
        options.add(new PropertyOption<RealLessThan>("will be less than") {

            private TextBox lessThanBox;

            @Override
            public Panel newEditPanel(RealLessThan v) {
                setValue(v);
                setWillBe();
                Panel p = new FlowPanel();
                lessThanBox = new PropertyTextBox();
                lessThanBox.setText(Double.toString(v.getValue()));
                p.add(lessThanBox);
                return p;
            }

            @Override
            public RealLessThan widgetToValue(RealLessThan r) {
                r.setValue(Double.valueOf(lessThanBox.getText()));
                return r;
            }

            @Override
            public boolean accepts(Value v) {
                return v.getClass().equals(RealLessThan.class);
            }

            @Override
            public RealLessThan newDefaultValue() {
                return new RealLessThan(0);
            }
        });
        options.add(new PropertyOption<RealBetween>("will be between") {

            private PropertyTextBox minBox;

            private PropertyTextBox maxBox;

            @Override
            public Panel newEditPanel(RealBetween v) {
                setValue(v);
                setWillBe();
                Panel p = new FlowPanel();
                minBox = new PropertyTextBox();
                minBox.setText(Double.toString(v.getMin()));
                p.add(minBox);
                p.add(new Label(" and "));
                maxBox = new PropertyTextBox();
                maxBox.setText(Double.toString(v.getMax()));
                p.add(maxBox);
                return p;
            }

            @Override
            public RealBetween widgetToValue(RealBetween r) {
                r.setMin(Double.parseDouble(minBox.getText()));
                r.setMax(Double.parseDouble(maxBox.getText()));
                return r;
            }

            @Override
            public boolean accepts(Value v) {
                return v.getClass().equals(RealBetween.class);
            }

            @Override
            public RealBetween newDefaultValue() {
                return new RealBetween(0, 1, true);
            }
        });
    }

    private String getUnitText(Unit unit) {
        if (unit == Unit.Distance) return "meters";
        if (unit == Unit.Mass) return "kilograms";
        if (unit == Unit.Speed) return "meters/second";
        if (unit == Unit.Volume) return "cm^3";
        if (unit == Unit.TimeDuration) return "seconds";
        if (unit == Unit.Currency) return "dollars";
        return "";
    }

    @Override
    protected void initPropertyPanel() {
        super.initPropertyPanel();
        unitLabel = new Label();
        unitLabel.addStyleName("PropertyLabel");
        if (getPropertyData() != null) {
            if (getPropertyData() instanceof RealVar) {
                RealVar rv = (RealVar) getPropertyData();
                Unit unit = rv.getUnit();
                if (unit != null) unitLabel.setText(getUnitText(unit));
            }
        }
        add(unitLabel);
    }
}
