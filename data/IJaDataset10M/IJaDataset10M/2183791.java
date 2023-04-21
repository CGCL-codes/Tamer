package com.softwoehr.pigiron.access.paramstructs;

import com.softwoehr.pigiron.access.*;

/**
 * ImageRecordArray implements the {@code image_record_array} from {@code Image_Create_DM}
 * @see com.softwoehr.pigiron.functions.ImageCreateDM
 */
public class ImageRecordArray extends VSMArray {

    /**
     * Create a modelled-for-read instance with a specified formal name.
     * @param formalName the formal name
     * @return the modelled instance.
     */
    public static ImageRecordArray modelArray(String formalName) {
        ImageRecordArray result = new ImageRecordArray();
        result.add(new ImageRecordStructure("image_record_structure"));
        result.setFormalName(formalName);
        return result;
    }

    /**
     * Create an instance by copying the value from a like instance, and
     * assign also the formal name.
     * @param value a like instance to copy from
     * @param formalName the formal name
     */
    public ImageRecordArray(VSMArray value, String formalName) {
        super(value, formalName);
    }

    /**
     * Create an instance by absorbing a CountedStruct type only if
     * that instance is the associated counted struct for this array
     * type. Assign also the formal name.
     * @param value a CountedStruct to absorb
     * @param formalName the formal name
     */
    public ImageRecordArray(CountedStruct value, String formalName) throws VSMArrayCountedStructCTORException {
        super();
        if (!value.getClass().getSimpleName().equals("ImageRecordStructureCounted")) {
            throw new VSMArrayCountedStructCTORException(value + " is not an instance of ImageRecordStructureCounted");
        }
        setValue(value);
        setFormalName(formalName);
    }

    /**
     * Create an instance by copying the value from a like instance.
     * @param value a like instance to copy from
     */
    public ImageRecordArray(VSMArray value) {
        super(value);
    }

    /**
     * Create an instance where only the formal name
     * is instanced.
     * @param formalName the formal name
     */
    public ImageRecordArray(String formalName) {
        super();
        setFormalName(formalName);
    }

    /**
     * Create an instance of undefined value.
     */
    public ImageRecordArray() {
    }

    /** Tests whether the Array can assimilate its proper
     * CountedStruct type and still not assimilate other CountedStruct types.
     * @param argv not used
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws VSMArrayCountedStructCTORException
     */
    public static void main(String argv[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, VSMArrayCountedStructCTORException {
        CountedStruct cS = (CountedStruct) Class.forName("com.softwoehr.pigiron.access.paramstructs.ImageRecordStructureCounted").newInstance();
        ImageRecordArray aC = new ImageRecordArray(cS, FORMAL_TYPE);
        System.out.println("Here is the ImageRecordArray instance having assimilated an ImageRecordStructureCounted instance: " + aC.prettyPrint());
        cS = new PageRangeStructureCounted();
        aC = new ImageRecordArray(cS, FORMAL_TYPE);
        System.out.println("You should never see this message due to a VSMArrayCountedStructCTORException thrown before: Here is the ImageRecordArray instance having assimilated an PageRangeStructureCounted instance: " + aC.prettyPrint());
    }
}
