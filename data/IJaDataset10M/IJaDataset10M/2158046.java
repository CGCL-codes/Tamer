package com.softwoehr.pigiron.access.paramstructs;

import com.softwoehr.pigiron.access.*;

/**
 * CpuInfoStructureCounted wrappers the {@code CpuInfoStructure} from {@code Image_Active_Configuration_Query}
 * as a PigIron CountedStruct pseudotype.
 * @see com.softwoehr.pigiron.functions.ImageActiveConfigurationQuery
 * @see com.softwoehr.pigiron.access.paramstructs.CpuInfoStructure
 */
public class CpuInfoStructureCounted extends CountedStruct {

    /**
     * Create an instance with a value derived by copying from a like instance
     * and instance its formal name at the same time.
     * null is legal value, means "just clear me and
     * re-initialize me with a valid list of yet-unread
     * parameters".
     * @param value a like instance to copy from
     * @param formalName the formal name
     * @see com.softwoehr.pigiron.access.CountedStruct
     */
    public CpuInfoStructureCounted(CountedStruct value, String formalName) {
        this(value);
        setFormalName(formalName);
    }

    /**
     * Create an instance with a value derived by copying from a like instance.
     * null is legal value, means "just clear me".
     * @param value a like instance to copy from
     */
    public CpuInfoStructureCounted(CountedStruct value) {
        super(value);
        if (value == null) {
            modelFormalParameters();
        }
    }

    /**
     * Create an instance with the formal name instanced
     * and the parameters modelled for reading.
     * @param formal_name the formal name of the instance
     */
    public CpuInfoStructureCounted(String formal_name) {
        super();
        setFormalName(formal_name);
        modelFormalParameters();
    }

    /**
     * Create a read-modelled instance.
     */
    public CpuInfoStructureCounted() {
        super();
        modelFormalParameters();
    }

    /**
     * Create an instance with all attributes instantiated
     * and instance its formal name at the same time.
     * This makes it easy to set up a VSMAPI input instance
     * of this structure.
     */
    public CpuInfoStructureCounted(VSMInt4 cpu_info_structure_length, CpuInfoStructure cpu_info_structure, String formalName) {
        super();
        add(cpu_info_structure_length);
        add(cpu_info_structure);
        setFormalName(formalName);
    }

    /**
     * Create a read-modelled instance.
     */
    public void modelFormalParameters() {
        clear();
        add(new VSMInt4(-1, "cpu_info_structure_length"));
        add(new CpuInfoStructure("cpu_info_structure"));
    }
}
