package ch.olsen.routes.atom.lib;

import java.util.Date;
import java.util.List;
import ch.olsen.products.util.configuration.BooleanProperty;
import ch.olsen.products.util.configuration.Configuration;
import ch.olsen.products.util.configuration.DateProperty;
import ch.olsen.products.util.configuration.DoubleProperty;
import ch.olsen.products.util.configuration.IntegerProperty;
import ch.olsen.products.util.configuration.LongProperty;
import ch.olsen.products.util.configuration.Property;
import ch.olsen.products.util.configuration.StringProperty;
import ch.olsen.products.util.configuration.TypedProperty;
import ch.olsen.routes.atom.AtomAbstr;
import ch.olsen.routes.atom.AtomException;
import ch.olsen.routes.atom.AtomInput;
import ch.olsen.routes.atom.AtomInputAbstr;
import ch.olsen.routes.atom.AtomOutput;
import ch.olsen.routes.atom.AtomOutputAbstr;
import ch.olsen.routes.atom.RoutesStep;
import ch.olsen.routes.cell.library.LibraryAutoDeploy;
import ch.olsen.routes.data.BooleanDataElement;
import ch.olsen.routes.data.DataElement;
import ch.olsen.routes.data.DataType;
import ch.olsen.routes.data.DateDataElement;
import ch.olsen.routes.data.DoubleDataElement;
import ch.olsen.routes.data.IntegerDataElement;
import ch.olsen.routes.data.LongDataElement;
import ch.olsen.routes.data.NullDataElement;
import ch.olsen.routes.data.StringDataElement;
import ch.olsen.routes.framework.RoutesFramework;

@LibraryAutoDeploy(name = "Constant", desc = "A constant value", path = "Main")
public class ConstantAtom extends AtomAbstr {

    private static final long serialVersionUID = 1L;

    private static final String desc = "A constant value";

    ConstantAtomConfiguration cfg = new ConstantAtomConfiguration();

    public AtomInput pulse;

    public AtomOutput constant;

    DataElement data;

    public ConstantAtom(RoutesFramework framework) {
        super(framework, desc);
        initialize();
    }

    public ConstantAtom(RoutesFramework framework, int n) {
        super(framework, desc);
        cfg.value.set(new IntegerProperty("value", "Value", n, -Integer.MAX_VALUE, Integer.MAX_VALUE, false));
        initialize();
    }

    public ConstantAtom(RoutesFramework framework, double n) {
        super(framework, desc);
        cfg.value.set(new DoubleProperty("value", "Value", n, -Double.MAX_VALUE, Double.MAX_VALUE, false));
        initialize();
    }

    public ConstantAtom(RoutesFramework framework, long n) {
        super(framework, desc);
        cfg.value.set(new LongProperty("value", "Value", n, -Long.MAX_VALUE, Long.MAX_VALUE, false));
        initialize();
    }

    public ConstantAtom(RoutesFramework framework, String s) {
        super(framework, desc);
        cfg.value.set(new StringProperty("value", "Value", s, false));
        initialize();
    }

    private void initialize() {
        pulse = new AtomInputAbstr(this, "pulse", "Propagates an event on the output containing the constant value") {

            private static final long serialVersionUID = 1L;

            public DataType getType() {
                return NullDataElement.Factory.getType();
            }

            public List<RoutesStep> receive_internal(DataElement data) throws AtomException {
                return constant.generateFireEvents();
            }
        };
        super.addInput(pulse);
        buildData();
        constant = new AtomOutputAbstr(this, "constant", "The Constant") {

            private static final long serialVersionUID = 1L;

            public DataElement get_internal() {
                return data;
            }

            public DataType getType() {
                return data.getType();
            }
        };
        super.addOutput(constant);
    }

    public void update(int n) {
        cfg.value.set(new IntegerProperty("value", "Value", n, -Integer.MAX_VALUE, Integer.MAX_VALUE, false));
        prepareForRunning();
    }

    public void update(double n) {
        cfg.value.set(new DoubleProperty("value", "Value", n, -Double.MAX_VALUE, Double.MAX_VALUE, false));
        prepareForRunning();
    }

    public void update(long n) {
        cfg.value.set(new LongProperty("value", "Value", n, -Long.MAX_VALUE, Long.MAX_VALUE, false));
        prepareForRunning();
    }

    public void update(String s) {
        cfg.value.set(new StringProperty("value", "Value", s, false));
        prepareForRunning();
    }

    protected void buildData() {
        if (cfg.value.get() instanceof IntegerProperty) {
            IntegerProperty p = (IntegerProperty) cfg.value.get();
            data = IntegerDataElement.Factory.newIntegerDE(p.value());
        } else if (cfg.value.get() instanceof LongProperty) {
            LongProperty p = (LongProperty) cfg.value.get();
            data = LongDataElement.Factory.newLongDE(p.value());
        } else if (cfg.value.get() instanceof DoubleProperty) {
            DoubleProperty p = (DoubleProperty) cfg.value.get();
            data = DoubleDataElement.Factory.newDoubleDE(p.value());
        } else if (cfg.value.get() instanceof DateProperty) {
            DateProperty p = (DateProperty) cfg.value.get();
            data = DateDataElement.Factory.newDateDE(p.value());
        } else if (cfg.value.get() instanceof BooleanProperty) {
            BooleanProperty p = (BooleanProperty) cfg.value.get();
            data = BooleanDataElement.Factory.newBooleanDE(p.booleanValue());
        } else if (cfg.value.get() instanceof StringProperty) {
            StringProperty p = (StringProperty) cfg.value.get();
            data = StringDataElement.Factory.newStringDE(p.value());
        }
    }

    @Override
    public void prepareForRunning() {
        buildData();
    }

    @Override
    public ConstantAtomConfiguration getParameters() {
        return cfg;
    }

    public static class ConstantAtomConfiguration extends Configuration<ConstantAtomConfiguration> {

        private static final long serialVersionUID = 1L;

        public TypedProperty<Property<?>> value;

        public ConstantAtomConfiguration() {
            value = new TypedProperty<Property<?>>("value", TypedProperty.getPrimitiveTypeFactories());
        }

        public void clear() {
        }

        public String getDescription() {
            return "Constant Value";
        }

        public String getName() {
            return "constant";
        }
    }

    public final String describe() {
        return "Constant";
    }

    @Override
    public void tryGuessFromAtomName(String name) {
        try {
            int n = Integer.parseInt(name);
            cfg.value.set(new IntegerProperty("value", "Value", n, -Integer.MAX_VALUE, Integer.MAX_VALUE, false));
            buildData();
            return;
        } catch (Exception e) {
        }
        try {
            double n = Double.parseDouble(name);
            cfg.value.set(new DoubleProperty("value", "Value", n, -Double.MAX_VALUE, Double.MAX_VALUE, false));
            buildData();
            return;
        } catch (Exception e) {
        }
        try {
            Date d = DateDataElement.Factory.parseDate(name);
            cfg.value.set(new DateProperty("value", "Value", d, false));
            buildData();
            return;
        } catch (Exception e) {
        }
    }
}
