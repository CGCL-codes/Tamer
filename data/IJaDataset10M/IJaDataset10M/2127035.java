package ctrlroot;

import org.springcontracts.dbc.annotation.Invariant;
import java.util.Vector;

@Invariant(condition = "(this.initialized) ==>  (this.dimension > 0 and not empty this.name)", message = "Pool schema and name must be initialized by Init method, initialized: " + " {this.initialized}, dimension: {this.dimension} ")
public interface IPool {

    boolean Delete(String sRecordID, String sSource, StringBuffer sStatus);

    boolean Update(String sRecordID, String[] tags, Object[] arglist, String sSource, StringBuffer sStatus);

    boolean Write(String sRecordID, String[] tags, Object[] arglist, String sOwner, StringBuffer sStatus);

    boolean EnumRecords(String[] sRecords, StringBuffer sStatus);

    boolean EnumOwnRecords(String[] sRecords, String sOwner, StringBuffer sStatus);

    boolean Read(String sRecordID, String[] tags, Object[] arglist, StringBuffer sStatus);

    int GetPublished(String sPublisher, StringBuffer sStatus);

    public Vector<EndPointAssociation> getAssociations();

    int GetSize(StringBuffer sStatus);

    public int getSize();

    public int getDimension();

    String getName();

    boolean getInitialized();

    public void init(String sName);

    void Associate(IComponent component, String sEndPoint);

    int GetDimension(StringBuffer sStatus);

    @SuppressWarnings({ "unchecked" })
    boolean EnumTags(String[] tags, Class[] types, StringBuffer sStatus);

    boolean VerifyWriteArguments(String[] tags, Object[] arglist, StringBuffer sStatus);

    boolean VerifyReadArguments(String[] tags, Object[] arglist, StringBuffer sStatus);

    boolean VerifyUpdateArguments(String[] tags, Object[] arglist, StringBuffer sStatus);
}
