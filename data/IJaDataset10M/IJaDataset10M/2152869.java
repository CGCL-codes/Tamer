package socialnetwork;

import java.util.ArrayList;

/** 
 * @author Terje Br�dland
 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PictureImpl implements IPicture {

    /** 
	 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String theFilename) {
        filename = theFilename;
    }

    /** 
	 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String theDescription) {
        description = theDescription;
    }

    /** 
	 * @uml.annotations for <code>album</code>
	 *     collection_type="Album"
	 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private ArrayList<IAlbum> album = new ArrayList<IAlbum>();

    public ArrayList<IAlbum> getAlbum() {
        return album;
    }

    public void setAlbum(ArrayList<IAlbum> theAlbum) {
        album = theAlbum;
    }

    /** 
	 * @uml.annotations for <code>comment</code>
	 *     collection_type="Comment"
	 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private ArrayList<IComment> comment = new ArrayList<IComment>();

    public ArrayList<IComment> getComment() {
        return comment;
    }

    public void setComment(ArrayList<IComment> theComment) {
        comment = theComment;
    }

    /** 
	 * @uml.annotations for <code>tagword</code>
	 *     collection_type="TagWord"
	 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private ArrayList<ITagWord> tagword = new ArrayList<ITagWord>();

    public ArrayList<ITagWord> getTagword() {
        return tagword;
    }

    public void setTagword(ArrayList<ITagWord> theTagword) {
        tagword = theTagword;
    }

    /** 
	 * @uml.annotations for <code>blogpost</code>
	 *     collection_type="BlogPost"
	 * @generated "UML to Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private ArrayList<IBlogPost> blogpost = new ArrayList<IBlogPost>();

    public ArrayList<IBlogPost> getBlogpost() {
        return blogpost;
    }

    public void setBlogpost(ArrayList<IBlogPost> theBlogpost) {
        blogpost = theBlogpost;
    }

    public void addTag(ITagWord tag) {
        this.tagword.add(tag);
    }

    public void removeTag(ITagWord tag) {
        this.tagword.remove(tag);
    }

    public void addComment(IComment comment) {
        this.comment.add(comment);
    }

    public void removeComment(IComment comment) {
        this.comment.remove(comment);
    }
}
