package ch.olsen.routes.cell.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.olsen.products.util.configuration.Configuration;
import ch.olsen.products.util.configuration.StringProperty;
import ch.olsen.routes.atom.Atom;
import ch.olsen.routes.atom.AtomAbstr;
import ch.olsen.routes.atom.AtomException;
import ch.olsen.routes.atom.AtomInput;
import ch.olsen.routes.atom.AtomInputAbstr;
import ch.olsen.routes.atom.AtomOutput;
import ch.olsen.routes.atom.AtomOutputAbstr;
import ch.olsen.routes.atom.LinkInput;
import ch.olsen.routes.atom.LinkOutput;
import ch.olsen.routes.atom.RoutesStep;
import ch.olsen.routes.cell.library.LibraryAutoDeploy;
import ch.olsen.routes.cell.service.CellServiceImpl.RemoteInputInCell;
import ch.olsen.routes.cell.service.CellServiceImpl.RemoteOutputInCell;
import ch.olsen.routes.data.DataElement;
import ch.olsen.routes.data.DataType;
import ch.olsen.routes.data.AggregatedDataElementImpl.AggregatedType;
import ch.olsen.routes.framework.RoutesFramework;
import ch.olsen.servicecontainer.naming.OsnURI;

/**
 * This atom functions as a bridge to a cell hosted in another (remote) place.
 * As configuration parameter it accepts the uri of the remote cell, and as soon
 * the uri is enteres (and prepareForRunning() is called), this atom will
 * replicate all the inputs/outputs of the remote cell and build remote bindings 
 * @author vito
 *
 */
@LibraryAutoDeploy(name = "RemoteCell", desc = "Establish a connection to a remote cell running in the " + "Service container.", path = "Main")
public class RemoteAtom extends AtomAbstr {

    private static final long serialVersionUID = 1L;

    RemoteCellConfiguration cfg = new RemoteCellConfiguration();

    transient AtomService remoteCell;

    public RemoteAtom(RoutesFramework framework) {
        super(framework, "Establish a connection to a remote cell running in the " + "Service container.");
    }

    public String describe() {
        return "Remote Cell";
    }

    @Override
    public RemoteCellConfiguration getParameters() {
        return cfg;
    }

    @Override
    public void prepareForRunning() {
        try {
            lookupRemoteCell();
            if (remoteCell == null) throw new NullPointerException();
            AggregatedType inputs = remoteCell.getInputs();
            AggregatedType outputs = remoteCell.getOutputs();
            for (AtomInput i : getAllInputs()) {
                int n = 0;
                for (; n < inputs.elems.length; n++) {
                    if (inputs.elems[n].name.equals(i.getName())) {
                        ((RemoteInput) i).type = inputs.elems[n].type;
                        break;
                    }
                }
                if (n == inputs.elems.length) {
                    if (i.getLink() != null) i.getLink().remove();
                    super.removeInput(i.getName());
                }
            }
            for (int n = 0; n < inputs.elems.length; n++) {
                boolean found = false;
                for (AtomInput i : getAllInputs()) {
                    if (inputs.elems[n].name.equals(i.getName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    createNewInput(inputs.elems[n].name, inputs.elems[n].type);
                }
            }
            for (AtomOutput o : getAllOutputs()) {
                int n = 0;
                for (; n < outputs.elems.length; n++) {
                    if (outputs.elems[n].name.equals(o.getName())) {
                        ((RemoteOutput) o).type = outputs.elems[n].type;
                        break;
                    }
                }
                if (n == outputs.elems.length) {
                    for (LinkInput l : o.getLinks()) l.remove();
                    super.removeOutput(o.getName());
                }
            }
            for (int n = 0; n < outputs.elems.length; n++) {
                boolean found = false;
                for (AtomOutput i : getAllOutputs()) {
                    if (outputs.elems[n].name.equals(i.getName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    createNewOutput(outputs.elems[n].name, outputs.elems[n].type);
                }
            }
        } catch (Exception e) {
        }
    }

    private void lookupRemoteCell() {
        if (remoteCell != null) return;
        try {
            remoteCell = (AtomService) lookupRemoteCell(new OsnURI(cfg.uri.value()));
        } catch (Exception e) {
        }
    }

    private void createNewInput(final String name, final DataType type) {
        RemoteInput input = new RemoteInput(this, name, type);
        super.addInput(input);
    }

    class RemoteInput extends AtomInputAbstr {

        DataType type;

        public RemoteInput(Atom atom, String name, DataType type) {
            super(atom, name, name);
            this.type = type;
        }

        private static final long serialVersionUID = 1L;

        public List<RoutesStep> receive_internal(DataElement data) throws AtomException {
            lookupRemoteCell();
            if (remoteCell == null) throw new AtomException("Cannot talk to remote cell " + cfg.uri.value());
            remoteCell.fireEvent(name, data);
            return null;
        }

        public DataType getType() {
            return type;
        }

        @Override
        public void setLink(LinkOutput link) throws AtomException {
            lookupRemoteCell();
            if (remoteCell == null) throw new AtomException("Cannot talk to remote cell " + cfg.uri.value());
            super.setLink(link);
            if (link != null) {
                RemoteOutputInCell remoteLink = (RemoteOutputInCell) framework.getOuputEntryPoint(link);
                remoteCell.linkInput(name, remoteLink);
            } else remoteCell.linkInput(name, null);
        }
    }

    private void createNewOutput(final String name, final DataType type) {
        RemoteOutput output = new RemoteOutput(this, name, type);
        super.addOutput(output);
    }

    class RemoteOutput extends AtomOutputAbstr {

        DataType type;

        Map<LinkInput, String> links = new HashMap<LinkInput, String>();

        public RemoteOutput(Atom atom, String name, DataType type) {
            super(atom, name, name);
            this.type = type;
        }

        private static final long serialVersionUID = 1L;

        public DataElement get_internal() throws AtomException {
            lookupRemoteCell();
            if (remoteCell == null) throw new AtomException("Cannot talk to remote cell " + cfg.uri.value());
            return remoteCell.pollOutput(name);
        }

        public DataType getType() {
            return type;
        }

        @Override
        public void addLink(LinkInput link) throws AtomException {
            lookupRemoteCell();
            if (remoteCell == null) throw new AtomException("Cannot talk to remote cell " + cfg.uri.value());
            super.addLink(link);
            RemoteInputInCell remoteLink = (RemoteInputInCell) framework.getInputEntryPoint(link);
            String linkName = remoteCell.linkOutput(name, remoteLink);
            links.put(link, linkName);
        }

        @Override
        public boolean removeLink(LinkInput link) throws AtomException {
            lookupRemoteCell();
            if (remoteCell == null) throw new AtomException("Cannot talk to remote cell " + cfg.uri.value());
            String linkName = links.get(link);
            if (linkName == null) throw new AtomException("Non existing link");
            return remoteCell.unLinkOutput(name, linkName);
        }
    }

    public static class RemoteCellConfiguration extends Configuration<RemoteCellConfiguration> {

        private static final long serialVersionUID = 1L;

        public StringProperty uri;

        public RemoteCellConfiguration() {
            uri = new StringProperty("uri", "URI of the remote cell", "osn://", false);
        }

        public void clear() {
        }

        public String getDescription() {
            return "Remote Cell";
        }

        public String getName() {
            return "remoteCell";
        }
    }
}
