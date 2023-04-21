package org.rubato.rubettes.builtin;

import static org.rubato.xml.XMLConstants.NETWORK;
import javax.swing.ImageIcon;
import org.rubato.base.AbstractRubette;
import org.rubato.base.RubatoConstants;
import org.rubato.base.Rubette;
import org.rubato.composer.RunInfo;
import org.rubato.composer.icons.Icons;
import org.rubato.composer.network.NetworkModel;
import org.rubato.composer.rubette.RubetteModel;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

public class MacroRubette extends AbstractRubette {

    public MacroRubette() {
        setInCount(0);
        setOutCount(0);
    }

    public void run(RunInfo runInfo) {
        if (inputRubette != null) {
            for (int i = 0; i < getInCount(); i++) {
                inputRubette.setValue(i, getInput(i));
            }
        }
        for (int i = 0; i < networkModel.getDependents().size(); i++) {
            if (runInfo.stopped()) {
                break;
            }
            RubetteModel model = networkModel.getDependents().get(i);
            Rubette rubette = model.getRubette();
            rubette.clearErrors();
            try {
                rubette.run(runInfo);
                if (rubette.hasErrors()) {
                    addError(Messages.getString("MacroRubette.error"));
                } else {
                    rubette.updateView();
                }
            } catch (Exception e) {
                addError(e);
            }
        }
        if (outputRubette != null) {
            for (int i = 0; i < getOutCount(); i++) {
                setOutput(i, outputRubette.getValue(i));
            }
        }
    }

    public void setNetworkModel(NetworkModel model) {
        networkModel = model;
        for (RubetteModel rmodel : model.getRubettes()) {
            Rubette arubette = rmodel.getRubette();
            if (arubette instanceof MacroInputRubette) {
                inputRubette = (MacroInputRubette) arubette;
            } else if (arubette instanceof MacroOutputRubette) {
                outputRubette = (MacroOutputRubette) arubette;
            }
        }
        networkModel.computeDependencyTree();
        if (inputRubette != null) {
            setInCount(inputRubette.getOutCount());
        } else {
            setInCount(0);
        }
        if (outputRubette != null) {
            setOutCount(outputRubette.getInCount());
        } else {
            setOutCount(0);
        }
    }

    public NetworkModel getNetworkModel() {
        return networkModel;
    }

    public String getGroup() {
        return RubatoConstants.MACRO_GROUP;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
        if (networkModel != null) {
            networkModel.setName(name);
        }
    }

    public void setInfo(String s) {
        info = s;
    }

    public boolean hasInfo() {
        return getInfo().length() != 0;
    }

    public String getInfo() {
        return info == null ? "" : info;
    }

    public Rubette newInstance() {
        return duplicate();
    }

    public Rubette duplicate() {
        MacroRubette newRubette = new MacroRubette();
        newRubette.setName(getName());
        newRubette.setInfo(getInfo());
        newRubette.setShortDescription(getShortDescription());
        newRubette.setLongDescription(getLongDescription());
        if (networkModel != null) {
            NetworkModel newModel = networkModel.newInstance();
            newRubette.setNetworkModel(newModel);
        }
        return newRubette;
    }

    public void setShortDescription(String s) {
        shortDesc = s;
    }

    public String getShortDescription() {
        return shortDesc;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setLongDescription(String s) {
        longDesc = s;
    }

    public String getLongDescription() {
        return longDesc;
    }

    public String getInTip(int i) {
        if (inputRubette != null) {
            return inputRubette.getOutTip(i);
        } else {
            return "";
        }
    }

    public String getOutTip(int i) {
        if (outputRubette != null) {
            return outputRubette.getInTip(i);
        } else {
            return "";
        }
    }

    private static final String INFO = "Info";

    private static final String SHORT = "ShortDescription";

    private static final String LONG = "LongDescription";

    public void toXML(XMLWriter writer) {
        if (getInfo().length() > 0) {
            writer.openBlock(INFO);
            writer.writeTextNode("\n" + getInfo() + "\n");
            writer.closeBlock();
        } else {
            writer.empty(INFO);
        }
        if (getShortDescription().length() > 0) {
            writer.openBlock(SHORT);
            writer.writeTextNode("\n" + getShortDescription() + "\n");
            writer.closeBlock();
        } else {
            writer.empty(SHORT);
        }
        if (getLongDescription().length() > 0) {
            writer.openBlock(LONG);
            writer.writeTextNode("\n" + getLongDescription() + "\n");
            writer.closeBlock();
        } else {
            writer.empty(LONG);
        }
        networkModel.toXML(writer);
    }

    public Rubette fromXML(XMLReader reader, Element element) {
        Element infoElement = XMLReader.getChild(element, INFO);
        String info0 = XMLReader.getText(infoElement).trim();
        Element shortElement = XMLReader.getNextSibling(infoElement, SHORT);
        String shortDesc0 = XMLReader.getText(shortElement).trim();
        Element longElement = XMLReader.getNextSibling(shortElement, LONG);
        String longDesc0 = XMLReader.getText(longElement).trim();
        Element networkElement = XMLReader.getNextSibling(longElement, NETWORK);
        NetworkModel model = NetworkModel.fromXML(reader, networkElement);
        MacroRubette rubette = new MacroRubette();
        rubette.setNetworkModel(model);
        rubette.setInfo(info0);
        rubette.setShortDescription(shortDesc0);
        rubette.setLongDescription(longDesc0);
        return rubette;
    }

    public String toString() {
        return "MacroRubette[" + networkModel + "]";
    }

    private String name = "MacroRubette";

    private String info = null;

    private String shortDesc = "";

    private String longDesc = "";

    private NetworkModel networkModel = null;

    private MacroInputRubette inputRubette = null;

    private MacroOutputRubette outputRubette = null;

    private static final ImageIcon icon;

    static {
        icon = Icons.loadIcon(MacroRubette.class, "neticon.png");
    }
}
