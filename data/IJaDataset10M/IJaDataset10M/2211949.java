package edu.psu.its.lionshare.database;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *       Maintains a URN history of files as their contents have changed.
 *       @author Lorin Metzger
 *     
*/
public class UrnHistory implements Serializable {

    /** identifier field */
    private Long id;

    /** persistent field */
    private Long fid;

    /** persistent field */
    private String newfurn;

    /** persistent field */
    private String oldfurn;

    /** full constructor */
    public UrnHistory(Long fid, String newfurn, String oldfurn) {
        this.fid = fid;
        this.newfurn = newfurn;
        this.oldfurn = oldfurn;
    }

    /** default constructor */
    public UrnHistory() {
    }

    public Long getId() {
        return this.id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    /** 
     *         The unique file id of the file on the peersever.
     *       
     */
    public Long getFid() {
        return this.fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    /** 
     *         The unique file SHA1 Hash that is generated new one.
     *       
     */
    public String getNewfurn() {
        return this.newfurn;
    }

    public void setNewfurn(String newfurn) {
        this.newfurn = newfurn;
    }

    /** 
     *         The unique file SHA1 Hash that is generated old one.
     *       
     */
    public String getOldfurn() {
        return this.oldfurn;
    }

    public void setOldfurn(String oldfurn) {
        this.oldfurn = oldfurn;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }
}
