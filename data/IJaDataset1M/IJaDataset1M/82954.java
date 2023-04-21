package alto.lang;

import alto.hash.Function;
import alto.io.Uri;
import alto.io.u.Bbuf;
import alto.io.u.Chbuf;
import alto.io.u.Objmap;
import alto.lang.sio.Field;

/**
 * <p> An address is a string of {@link Component components} in
 * referece to a storage location.  </p>
 * 
 * <h3>Sio</h3>
 * 
 * In Sio format, an address is a record containing component fields.
 * 
 * 
 * @author jdp
 * @see Reference
 * @since 1.6
 */
public class Address extends alto.sys.PSioFile implements Sio.Type.Record {

    public static class List {

        public static final Address[] Add(Address[] list, Address c) {
            if (null == c) return list; else if (null == list) return new Address[] { c }; else {
                int len = list.length;
                Address[] copier = new Address[len + 1];
                System.arraycopy(list, 0, copier, 0, len);
                copier[len] = c;
                return copier;
            }
        }

        public static final Address[][] Add(Address[][] list, Address[] c) {
            if (null == c) return list; else if (null == list) return new Address[][] { c }; else {
                int len = list.length;
                Address[][] copier = new Address[len + 1][];
                System.arraycopy(list, 0, copier, 0, len);
                copier[len] = c;
                return copier;
            }
        }

        public static final boolean Equals(Address[] a, Address[] b) {
            if (null == a) return (null == b); else if (null == b) return false; else if (a.length == b.length) {
                for (int cc = 0, count = a.length; cc < count; cc++) {
                    if (!a.equals(b)) return false;
                }
                return true;
            } else return false;
        }
    }

    /**
     * Address reference URI has prefix <code>"store:"</code>.
     */
    public static final java.lang.String PREFIX = "store:";

    public static final java.lang.String Nil = null;

    public static final boolean Required = true;

    public static final boolean RequiredNot = false;

    public static final boolean PathU = true;

    public static final boolean PathUNot = false;

    /**
     * Tools used by the Address constructor.
     */
    public static class Ctor {

        public static final java.lang.String Parent(java.lang.String path) {
            if (null == path || 1 > path.length()) return null; else {
                int ix1 = path.lastIndexOf('/');
                if (-1 < ix1) return path.substring(0, ix1); else return null;
            }
        }

        public static final java.lang.String CleanTail(java.lang.String string) {
            int term = (string.length() - 1);
            if (-1 < term) {
                while ('/' == string.charAt(term)) {
                    string = string.substring(0, term);
                    term = (string.length() - 1);
                    if (0 > term) return null;
                }
                return string;
            } else return null;
        }

        public static final java.lang.String Hex(byte[] value) {
            if (null != value) {
                return alto.io.u.Hex.encode(value);
            } else throw new IllegalArgumentException();
        }

        public static final java.lang.String Hex(int value) {
            if (0 <= value) return alto.io.u.Hex.encode(value); else throw new IllegalArgumentException(java.lang.String.valueOf(value));
        }

        public static final java.lang.String Hex(long value) {
            if (0L <= value) return alto.io.u.Hex.encode(value); else throw new IllegalArgumentException(java.lang.String.valueOf(value));
        }

        public static final java.lang.String Hex(Component value) {
            if (null != value) return value.toString(); else throw new IllegalArgumentException();
        }
    }

    protected java.lang.String uri, host, path, reference, path_storage, path_storage_parent;

    protected Uri parser;

    protected Component[] address;

    protected int hashCode;

    protected boolean attributes, persistent, transactional;

    /**
     * Sio address content constructor as for sio group.
     */
    public Address(alto.io.Input sio) throws java.io.IOException {
        super(sio);
    }

    /**
     * Sio address content field constructor as for sio record.
     * @see PSioFile
     * @see RType
     */
    protected Address(alto.sys.Reference ref) throws java.io.IOException {
        super(ref);
    }

    /**
     * Address reference constructor.
     * @see alto.sys.Reference
     */
    public Address(Component container, Component type, Component path) {
        this(Component.Relation.U, container, type, path);
    }

    public Address(Component relation, Component container, Component type, Component path) {
        this(relation, container, type, path, Component.Version.Current);
    }

    public Address(Component relation, Component container, Component type, Component path, Component version) {
        super();
        Component[] address = null;
        address = Component.List.Add(address, relation);
        address = Component.List.Add(address, container);
        address = Component.List.Add(address, type);
        address = Component.List.Add(address, path);
        address = Component.List.Add(address, version);
        this.setAddress(address);
    }

    public Address(Component[] address) {
        super();
        if (null != address) this.setAddress(address); else throw new alto.sys.Error.Argument("Null argument");
    }

    public Address(Component[] prefix, Component suffix) {
        this(Component.List.Add(prefix, suffix));
    }

    public Address(Component container, alto.lang.Type type, java.lang.String path) {
        this(container, type.getAddressComponent(), Component.Path.Tools.ValueOf(type, path));
        this.path = Component.Tools.Path(path);
    }

    public Address(Address prefix, java.lang.String p) {
        this(Component.Tools.For(prefix.getAddressComponents(), p));
    }

    public Address(String uri) {
        this(new alto.io.u.Uri(uri));
    }

    public Address(Uri parser) {
        super();
        this.parser = parser;
        this.host = parser.getHostName();
        this.path = Component.Tools.Path(parser.getPath());
        Component[] address = Component.Tools.AddressNumericFrom(parser);
        if (null != address) this.setAddress(address); else {
            address = Component.Tools.AddressSymbolicFrom(parser);
            if (null != address) this.setAddress(address); else throw new alto.sys.Error.Argument("Inaddressable location '" + parser.toString() + "'.");
        }
    }

    public final Uri getCreateParser() {
        Uri parser = this.parser;
        if (null == parser) {
            java.lang.String string = this.uri;
            if (null != string) {
                parser = new alto.io.u.Uri(string);
                this.parser = parser;
            }
        }
        return parser;
    }

    /**
     * @return A reference URI for this address, using all of the
     * information available.  In public user space if possible.
     */
    public final java.lang.String getAddressReference() {
        java.lang.String reference = this.reference;
        if (null == reference) {
            Uri parser = this.parser;
            if (null != parser) {
                java.lang.String host = parser.getHostName();
                if (null == host) reference = this.uri; else {
                    java.lang.String path = parser.getPath();
                    if (null == path) reference = this.uri; else {
                        java.lang.String scheme = parser.getScheme();
                        if (null == scheme) scheme = "http:";
                        reference = scheme + "//" + Chbuf.fcat(host, path);
                    }
                }
            } else {
                java.lang.String host = this.host;
                java.lang.String path = this.path;
                if (null != host && null != path) reference = "http://" + Chbuf.fcat(host, path); else reference = this.uri;
            }
            this.reference = reference;
        }
        return reference;
    }

    public final java.lang.String toReference(alto.sys.IO.Location location) {
        java.lang.String host = location.getName();
        java.lang.String path = this.getPathStorage();
        return "http://" + Chbuf.fcat(host, path);
    }

    public final boolean hasUri() {
        return (null != this.parser);
    }

    public final Uri getUri() {
        return this.parser;
    }

    public final boolean hasHostPath() {
        return (null != this.host && null != this.path);
    }

    public final java.lang.String getHostName() {
        return this.host;
    }

    public final java.lang.String getPath() {
        return this.path;
    }

    public final java.lang.String getPathParent() {
        return this.getCreateParser().getPathParent();
    }

    public final boolean hasPathStorage() {
        return (null != this.path_storage);
    }

    public final boolean hasNotPathStorage() {
        return (null == this.path_storage);
    }

    /**
     * @return String <code>"/C/X/X/X"</code> 
     */
    public final java.lang.String getPathStorage() {
        return this.path_storage;
    }

    public final java.lang.String getPathStorageParent() {
        java.lang.String path = this.path_storage_parent;
        if (null == path) {
            path = Ctor.Parent(this.path_storage);
            this.path_storage_parent = path;
        }
        return path;
    }

    public final Uri getParser() {
        return this.parser;
    }

    /**
     * @return Concatenation of address component list bytes up to and
     * including the path component.
     */
    public final byte[] getAddressPathBytes() {
        try {
            Bbuf buf = new Bbuf();
            Component[] address = this.address;
            for (int cc = 0, count = Math.min(address.length, Component.Path.LengthWith); cc < count; cc++) {
                buf.append(address[cc].toByteArray());
            }
            return buf.toByteArray();
        } catch (java.io.IOException api) {
            throw new alto.sys.Error.State(api);
        }
    }

    protected final boolean attributes() {
        if (this.attributes) return true; else {
            Component.Type classValue = this.getComponentClass();
            alto.lang.Type classType = classValue.getType();
            if (null != classType) {
                this.persistent = classType.isTypePersistent();
                this.transactional = classType.isTypeTransactional();
                this.attributes = true;
                return true;
            } else return false;
        }
    }

    public final boolean isPersistent() {
        if (this.attributes()) return this.persistent; else throw new alto.sys.Error.State.Init();
    }

    public final boolean isNotPersistent() {
        return (!this.isPersistent());
    }

    public final boolean isTransient() {
        return (!this.isPersistent());
    }

    public final boolean isTransactional() {
        if (this.attributes()) return this.transactional; else throw new alto.sys.Error.State.Init();
    }

    public final boolean isNotTransactional() {
        return (!this.isTransactional());
    }

    /**
     * Address to resource "current" version.
     */
    public final boolean isAddressToCurrent() {
        return (Component.Version.Current.equals(this.getComponentTerminal()));
    }

    public final boolean isNotAddressToCurrent() {
        return (!(Component.Version.Current.equals(this.getComponentTerminal())));
    }

    public final boolean isAddressToTemporary() {
        return (Component.Version.Temporary.equals(this.getComponentTerminal()));
    }

    public final boolean isNotAddressToTemporary() {
        return (!(Component.Version.Temporary.equals(this.getComponentTerminal())));
    }

    public final boolean isAddressToNamed() {
        return (this.getComponentTerminal() instanceof Component.Named);
    }

    public final boolean hasAddressComponents() {
        return (null != this.address);
    }

    public final boolean hasNotAddressComponents() {
        return (null == this.address);
    }

    public final Component[] getAddressComponents() {
        return this.address;
    }

    public final boolean hasComponent(int idx) {
        if (0 > idx) throw new IllegalArgumentException(String.valueOf(idx)); else {
            Component[] address = this.address;
            return (null != address && idx < address.length);
        }
    }

    public final Component getComponent(int idx) {
        if (0 > idx) throw new IllegalArgumentException(String.valueOf(idx)); else {
            Component[] address = this.address;
            if (null != address && idx < address.length) return address[idx]; else return null;
        }
    }

    /**
     * @see #hasComponentClass()
     */
    public final Component[] getAddressClass() {
        Component[] address = this.address;
        int len = Component.Type.LengthWith;
        if (len == address.length) return address; else if (len < address.length) {
            Component[] copy = new Component[len];
            System.arraycopy(address, 0, copy, 0, len);
            return copy;
        } else throw new alto.sys.Error.State("Missing address depth");
    }

    /**
     * @see #hasComponentPath()
     */
    public final Component[] getAddressPath() {
        Component[] address = this.address;
        int len = Component.Path.LengthWith;
        if (len == address.length) return address; else if (len < address.length) {
            Component[] copy = new Component[len];
            System.arraycopy(address, 0, copy, 0, len);
            return copy;
        } else throw new alto.sys.Error.State("Missing address depth");
    }

    public final Address toRelation(Component.Relation relation) {
        if (this.inRelation(relation)) return this; else {
            Component[] address = this.address;
            int len = address.length;
            if (0 < len) {
                Component[] copy = new Component[len];
                System.arraycopy(address, 0, copy, 0, len);
                copy[Component.Relation.Position] = relation;
                return new Address(copy);
            } else {
                Component[] copy = new Component[len];
                System.arraycopy(address, 0, copy, 0, len);
                copy[Component.Relation.Position] = relation;
                return new Address(copy);
            }
        }
    }

    public final boolean hasComponentRelation() {
        return this.hasComponent(Component.Relation.Position);
    }

    /**
     * Semantically identical to {@link #getComponentHead()}, this
     * method name has the notational value of indicating the intent
     * to look at the storage relation class of the address.
     * @see Address$Storage$Class
     */
    public final Component.Relation getComponentRelation() {
        return (Component.Relation) this.getComponent(Component.Relation.Position);
    }

    /**
     * @return Is not address to content / data object
     */
    public final boolean isAddressToRelation() {
        return (Component.Relation.LengthWith == address.length);
    }

    public final boolean inRelation(Component c) {
        Component.Relation relation = this.getComponentRelation();
        if (null != relation) return (relation == c || relation.equals(c)); else return false;
    }

    public final boolean hasComponentContainer() {
        return this.hasComponent(Component.Host.Position);
    }

    public final Component.Host getComponentContainer() {
        return (Component.Host) this.getComponent(Component.Host.Position);
    }

    /**
     * @return Is not address to content / data object
     */
    public final boolean isAddressToContainer() {
        Component[] address = this.address;
        return (Component.Host.LengthWith == address.length);
    }

    public final boolean inContainer(Component c) {
        Component.Host host = Component.Host.Tools.From(this.address);
        if (null != host) return (host == c || host.equals(c)); else return false;
    }

    public final boolean inContainerOf(Address that) {
        if (null != that) {
            Component.Host thisAddress = this.getComponentContainer();
            if (null != thisAddress) {
                Component.Host thatAddress = that.getComponentContainer();
                if (null != thatAddress) return thisAddress.equals(thatAddress);
            }
        }
        return false;
    }

    public final boolean hasComponentClass() {
        return (Component.Type.Position < this.address.length);
    }

    public final Component.Type getComponentClass() {
        return (Component.Type) this.getComponent(Component.Type.Position);
    }

    /**
     * @return Is not address to content / data object
     */
    public final boolean isAddressToClass() {
        return (Component.Type.LengthWith == this.address.length);
    }

    public final Address toAddressClass(Component.Type type) {
        if (this.inClass(type)) return this; else {
            Component[] address = this.address;
            int len = address.length;
            if (0 < len) {
                Component[] copy = new Component[len];
                System.arraycopy(address, 0, copy, 0, len);
                copy[Component.Type.Position] = type;
                return new Address(copy);
            } else {
                Component[] copy = new Component[len];
                System.arraycopy(address, 0, copy, 0, len);
                copy[Component.Type.Position] = type;
                return new Address(copy);
            }
        }
    }

    public final boolean hasComponentPath() {
        return (null != Component.Path.Tools.From(this.address));
    }

    public final Component.Path getComponentPath() {
        return (Component.Path) this.getComponent(Component.Path.Position);
    }

    /**
     * @return Is not address to content / data object
     */
    public final boolean isAddressToPath() {
        return (Component.Path.LengthWith == this.address.length);
    }

    public final boolean hasComponentTerminal() {
        return (Component.Version.LengthWith <= this.address.length);
    }

    public final boolean hasNotComponentTerminal() {
        return (Component.Version.LengthWith > this.address.length);
    }

    public final Component.Version getComponentTerminal() {
        return (Component.Version) this.getComponent(Component.Version.Position);
    }

    /**
     * @return Is address to content / data object
     */
    public final boolean isAddressToTerminal() {
        Component[] address = this.address;
        int len = Component.Version.LengthWith;
        return (null != address && len == address.length);
    }

    /**
     * @return The head of the component list
     */
    public final Component getComponentHead() {
        Component[] address = this.address;
        if (null != address) return address[0]; else return null;
    }

    /**
     * @return The tail of the component list
     */
    public final Component getComponentTail() {
        Component[] address = this.address;
        if (null != address) return address[address.length - 1]; else return null;
    }

    public final boolean isAddressBasePath() {
        Component[] address = this.address;
        if (null != address) return (Component.Path.LengthWith == address.length); else return false;
    }

    public final Address getAddressBasePath() {
        Component[] address = this.address;
        int len = Component.Path.LengthWith;
        if (len == address.length) return this; else if (len < address.length) {
            Component[] copy = new Component[len];
            System.arraycopy(address, 0, copy, 0, len);
            return new Address(copy);
        } else return null;
    }

    public final boolean inClass(Component c) {
        if (null != c) {
            Component.Type valueClass = this.getComponentClass();
            if (null != valueClass) {
                return valueClass.equals(c);
            }
        }
        return false;
    }

    public final boolean inClass(byte[] b) {
        if (null != b) {
            Component.Type valueClass = this.getComponentClass();
            if (null != valueClass) {
                byte[] a = valueClass.toByteArray();
                if (null != a) return Bbuf.equals(a, b);
            }
        }
        return false;
    }

    public final boolean inClass(alto.lang.Type type) {
        Component.Type b = type.getAddressComponent();
        return this.inClass(b);
    }

    public boolean nocache() {
        return this.hasNotComponentTerminal();
    }

    public final void readMessage(alto.io.Input in) throws java.io.IOException {
        Component[] address = null;
        while (true) {
            try {
                byte[] cb = Field.Read(in);
                if (null != cb) {
                    address = Component.List.Add(address, cb);
                }
            } catch (Sio.Error end) {
                break;
            }
        }
        this.setAddress(address);
    }

    public final void writeMessage(alto.io.Output out) throws java.io.IOException {
        Component[] address = this.address;
        if (null != address) {
            for (int cc = 0, count = address.length; cc < count; cc++) {
                address[cc].sioWrite(out);
            }
        }
    }

    protected void setAddress(Address address) {
        this.setAddress(address.getAddressComponents());
    }

    protected void setAddress(Component[] address) {
        if (null != address) {
            this.address = address;
            this.path_storage = Component.Tools.PathStorageFor(address);
            this.uri = PREFIX + this.path_storage;
            this.hashCode = alto.hash.Function.Djb.Hash32(Component.Tools.Cat(address));
        }
    }

    public final java.lang.String toString() {
        return this.uri;
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public final boolean equals(Object ano) {
        if (ano == this) return true; else if (null == ano) return false; else if (ano instanceof Address) {
            Address anoa = (Address) ano;
            return Component.List.Equals(this.address, anoa.address);
        } else return false;
    }
}
