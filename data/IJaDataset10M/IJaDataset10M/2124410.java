package org.apache.jetspeed.om.registry;

/**
 * Interface for storing meta-info on a registry entry
 * 
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @version $Id: MetaInfo.java,v 1.2 2004/02/23 03:11:39 jford Exp $
 */
public interface MetaInfo {

    /** @return the title for this entry */
    public String getTitle();

    /** Sets the title for this entry
     * @param title the new title for this entry
     */
    public void setTitle(String title);

    /** @return the description for this entry */
    public String getDescription();

    /** Sets the description for this entry
     * @param description the new description for this entry
     */
    public void setDescription(String description);

    /** @return the image link for this entry */
    public String getImage();

    /** Sets the image URL attached to this entry
     * @param image the image URL to link to this entry
     */
    public void setImage(String image);
}
