package mipt.aaf.edit.data;

public interface PartedDataEditDelegate extends DataEditDelegate {

    /**
 * This method must update the view using part's fields get by part.getData().get*(*)
 * [If some of you data fields are swizzling references (Data) they should be swizzled here.
 *  Note that for many Data implementations dataType must be known for swizzling:
 *  in this case use ((TypedDataField)part.getData()).getData(dataType, *) ]
 * Note that you can't use part.getCurrent() here: it is initialized with swizzled fields!
 * @param partIndex int
 */
    void updatePartView(int partIndex, PartedDataEditor.Part part);
}
