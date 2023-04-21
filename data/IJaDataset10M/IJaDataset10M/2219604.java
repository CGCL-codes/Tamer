package info.repo.didl;

/**
 * The <code>DIDLInfoType</code> allows information applicable only to the 
 * DIDL document. A DIDLInfoType may contain any data inside. A DIDLInfoType 
 * must not have attributes. 
 *
 * @author Xiaoming Liu <liu_x@lanl.gov>
 * @author Patrick Hochstenbach <patrick.hochstenbach@ugent.be>
 * @author Kjell Lotigiers <kjell.lotigiers@ugent.be>
 */
public interface DIDLInfoType extends DIDLBaseType {

    public Object getContent();

    public void setContent(Object content);
}
