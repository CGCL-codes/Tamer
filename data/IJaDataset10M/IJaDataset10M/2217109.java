package ch.olsen.routes.cell.service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import ch.olsen.products.util.configuration.BooleanProperty;
import ch.olsen.products.util.configuration.Configuration;
import ch.olsen.products.util.configuration.DateProperty;
import ch.olsen.products.util.configuration.DoubleProperty;
import ch.olsen.products.util.configuration.EnumProperty;
import ch.olsen.products.util.configuration.IntegerProperty;
import ch.olsen.products.util.configuration.ListOfUnnamedProperties;
import ch.olsen.products.util.configuration.LongProperty;
import ch.olsen.products.util.configuration.Property;
import ch.olsen.products.util.configuration.StringProperty;
import ch.olsen.products.util.configuration.TypedProperty;
import ch.olsen.routes.cell.service.gwt.client.AtomModel.CfgElem;
import ch.olsen.routes.cell.service.gwt.client.AtomModel.EnumCfgElem;
import ch.olsen.routes.cell.service.gwt.client.AtomModel.StringCfgElem;
import ch.olsen.routes.cell.service.gwt.client.CellServerIfc.AtomViewException;

public class ConfigurationTools {

    private static final String addAction = "Add";

    private static final String removeAction = "Remove";

    /**
	 * 
	 * @param cfg the cfg to convert
	 * @param cfgElems the output
	 */
    public final void convertConfiguration(Configuration<? extends Configuration> cfg, List<CfgElem> cfgElems) {
        convert("", cfg.properties(), cfgElems, new String[] {}, false);
    }

    private final void convert(String prefix, Iterable<Property<? extends Property>> properties, List<CfgElem> cfgElems, String actions[], boolean addCounterToName) {
        int counter = 1;
        for (Property<? extends Property> p : properties) {
            final String nameHere;
            if (addCounterToName) nameHere = prefix + counter; else nameHere = prefix + p.getName();
            if (p instanceof StringProperty) {
                StringProperty sp = (StringProperty) p;
                StringCfgElem sc = new StringCfgElem();
                sc.help = sp.getDescription();
                sc.name = nameHere;
                sc.value = sp.value();
                sc.actions = actions;
                cfgElems.add(sc);
            } else if (p instanceof IntegerProperty) {
                IntegerProperty sp = (IntegerProperty) p;
                StringCfgElem sc = new StringCfgElem();
                sc.help = sp.getDescription();
                sc.name = nameHere;
                sc.value = Integer.toString(sp.value());
                sc.actions = actions;
                cfgElems.add(sc);
            } else if (p instanceof DoubleProperty) {
                DoubleProperty sp = (DoubleProperty) p;
                StringCfgElem sc = new StringCfgElem();
                sc.help = sp.getDescription();
                sc.name = nameHere;
                sc.value = Double.toString(sp.value());
                sc.actions = actions;
                cfgElems.add(sc);
            } else if (p instanceof LongProperty) {
                LongProperty sp = (LongProperty) p;
                StringCfgElem sc = new StringCfgElem();
                sc.help = sp.getDescription();
                sc.name = nameHere;
                sc.value = Long.toString(sp.value());
                sc.actions = actions;
                cfgElems.add(sc);
            } else if (p instanceof DateProperty) {
                DateProperty sp = (DateProperty) p;
                StringCfgElem sc = new StringCfgElem();
                sc.help = sp.getDescription();
                sc.name = nameHere;
                sc.value = DateProperty.getStaticDateFormat().format(sp.value());
                sc.actions = actions;
                cfgElems.add(sc);
            } else if (p instanceof EnumProperty) {
                EnumProperty sp = (EnumProperty) p;
                EnumCfgElem sc = new EnumCfgElem();
                sc.help = sp.getDescription();
                sc.name = nameHere;
                sc.values = sp.values();
                sc.selected = sp.value().toString();
                sc.actions = actions;
                cfgElems.add(sc);
            } else if (p instanceof BooleanProperty) {
                BooleanProperty sp = (BooleanProperty) p;
                EnumCfgElem sc = new EnumCfgElem();
                sc.help = sp.getDescription();
                sc.name = nameHere;
                sc.values = new String[] { "Yes", "No" };
                sc.selected = sc.values[sp.booleanValue() ? 0 : 1];
                sc.actions = actions;
                cfgElems.add(sc);
            } else if (p instanceof ListOfUnnamedProperties) {
                ListOfUnnamedProperties lp = (ListOfUnnamedProperties) p;
                CfgElem c = new CfgElem();
                c.help = lp.getDescription();
                c.name = nameHere;
                c.actions = new String[actions.length + 1];
                for (int n = 0; n < actions.length; n++) c.actions[n] = actions[n];
                c.actions[actions.length] = addAction;
                cfgElems.add(c);
                convert(nameHere + "-", lp.getProperties(), cfgElems, new String[] { removeAction }, true);
            } else if (p instanceof Configuration) {
                Configuration cp = (Configuration) p;
                CfgElem c = new CfgElem();
                c.help = cp.getDescription();
                c.name = nameHere;
                cfgElems.add(c);
                c.actions = actions;
                convert(nameHere + "-", cp.properties(), cfgElems, new String[] {}, false);
            } else if (p instanceof TypedProperty) {
                TypedProperty<? extends Property> tp = (TypedProperty<? extends Property>) p;
                CfgElem c = new CfgElem();
                c.help = tp.getDescription();
                c.name = nameHere;
                cfgElems.add(c);
                c.actions = new String[actions.length + tp.getFactories().size()];
                int n = 0;
                for (; n < actions.length; n++) c.actions[n] = actions[n];
                for (String f : tp.getFactories()) {
                    c.actions[n] = f;
                    n++;
                }
                Collection<Property<? extends Property>> sub = new LinkedList<Property<? extends Property>>();
                sub.add(tp.get());
                convert(nameHere + "-", sub, cfgElems, new String[] {}, false);
            }
            counter++;
        }
    }

    public final void updateCfg(Configuration<? extends Configuration> cfg, String propName, String value) throws AtomViewException {
        boolean found = updateCfgItem("", cfg.properties(), propName, value, false);
        if (!found) throw new AtomViewException("Could not update property");
    }

    private final boolean updateCfgItem(String prefix, Iterable<Property<? extends Property>> properties, String propName, String value, boolean useCounterForName) throws AtomViewException {
        boolean found = false;
        int counter = 1;
        for (Property<? extends Property> p : properties) {
            String nameHere = useCounterForName ? prefix + counter : prefix + p.getName();
            if (propName.equals(nameHere)) {
                if (p instanceof StringProperty) {
                    StringProperty sp = (StringProperty) p;
                    sp.set(value);
                    found = true;
                } else if (p instanceof IntegerProperty) {
                    IntegerProperty sp = (IntegerProperty) p;
                    try {
                        sp.set(Integer.parseInt(value));
                        found = true;
                    } catch (Exception e) {
                        throw new AtomViewException(e.getMessage());
                    }
                } else if (p instanceof DoubleProperty) {
                    DoubleProperty sp = (DoubleProperty) p;
                    try {
                        sp.set(Double.parseDouble(value));
                        found = true;
                    } catch (Exception e) {
                        throw new AtomViewException(e.getMessage());
                    }
                } else if (p instanceof LongProperty) {
                    LongProperty sp = (LongProperty) p;
                    try {
                        sp.set(Long.parseLong(value));
                        found = true;
                    } catch (Exception e) {
                        throw new AtomViewException(e.getMessage());
                    }
                } else if (p instanceof DateProperty) {
                    DateProperty sp = (DateProperty) p;
                    try {
                        sp.set(DateProperty.getStaticDateFormat().parse(value));
                        found = true;
                    } catch (Exception e) {
                        throw new AtomViewException(e.getMessage());
                    }
                } else if (p instanceof EnumProperty) {
                    EnumProperty sp = (EnumProperty) p;
                    try {
                        sp.set(value);
                        found = true;
                    } catch (Exception e) {
                        throw new AtomViewException(e.getMessage());
                    }
                } else if (p instanceof BooleanProperty) {
                    BooleanProperty sp = (BooleanProperty) p;
                    try {
                        if (value.equals("Yes")) sp.set(true); else sp.set(false);
                        found = true;
                    } catch (Exception e) {
                        throw new AtomViewException(e.getMessage());
                    }
                }
            } else if (propName.startsWith(nameHere)) {
                if (p instanceof ListOfUnnamedProperties) {
                    ListOfUnnamedProperties lp = (ListOfUnnamedProperties) p;
                    found = updateCfgItem(nameHere + "-", lp.getProperties(), propName, value, true);
                } else if (p instanceof Configuration) {
                    Configuration cp = (Configuration) p;
                    found = updateCfgItem(nameHere + "-", cp.properties(), propName, value, false);
                } else if (p instanceof TypedProperty) {
                    TypedProperty<? extends Property> tp = (TypedProperty<? extends Property>) p;
                    Collection<Property<? extends Property>> sub = new LinkedList<Property<? extends Property>>();
                    sub.add(tp.get());
                    found = updateCfgItem(nameHere + "-", sub, propName, value, false);
                }
            }
            counter++;
        }
        return found;
    }

    public void action(Configuration<? extends Configuration> cfg, String propName, String actionName) throws AtomViewException {
        try {
            action("", cfg.properties(), propName, actionName, false);
        } catch (ActionException e) {
            throw new AtomViewException("Action not defined");
        }
    }

    private final void action(String prefix, Iterable<Property<? extends Property>> properties, String propName, String actionName, boolean useCounterForName) throws AtomViewException, ActionException {
        int counter = 1;
        for (Property<? extends Property> p : properties) {
            String nameHere = useCounterForName ? prefix + counter : prefix + p.getName();
            if (propName.startsWith(nameHere)) {
                if (p instanceof ListOfUnnamedProperties) {
                    ListOfUnnamedProperties lp = (ListOfUnnamedProperties) p;
                    if (propName.equals(nameHere)) {
                        if (actionName.equals(addAction)) {
                            lp.add(null);
                        } else throw new ActionException(p);
                    } else {
                        try {
                            action(nameHere + "-", lp.getProperties(), propName, actionName, true);
                        } catch (ActionException e) {
                            if (actionName.equals(removeAction)) {
                                lp.remove(e.property);
                            } else throw new AtomViewException("Action not defined!");
                        }
                    }
                } else if (p instanceof Configuration) {
                    Configuration cp = (Configuration) p;
                    if (propName.equals(nameHere)) {
                        throw new ActionException(p);
                    } else {
                        action(nameHere + "-", cp.properties(), propName, actionName, false);
                    }
                } else if (p instanceof TypedProperty) {
                    TypedProperty<? extends Property> tp = (TypedProperty<? extends Property>) p;
                    if (propName.equals(nameHere)) {
                        boolean found = false;
                        for (String f : tp.getFactories()) {
                            if (f.equals(actionName)) {
                                found = true;
                                if (tp.setByFactory(f) == null) throw new AtomViewException("Action not defined");
                            }
                        }
                        if (!found) throw new ActionException(p);
                    } else {
                        Collection<Property<? extends Property>> sub = new LinkedList<Property<? extends Property>>();
                        sub.add(tp.get());
                        action(nameHere + "-", sub, propName, actionName, false);
                    }
                } else {
                    if (propName.equals(nameHere)) {
                        throw new ActionException(p);
                    } else throw new AtomViewException("Action not defined!");
                }
            }
            counter++;
        }
    }

    static class ActionException extends Exception {

        final Property property;

        public ActionException(Property property) {
            this.property = property;
        }

        private static final long serialVersionUID = 1L;
    }
}
