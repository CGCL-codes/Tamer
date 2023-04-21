package ti.targetinfo.symtable.bfdw;

public class bfd_tdata {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected bfd_tdata(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(bfd_tdata obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            bfdwJNI.delete_bfd_tdata(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setAout_data(SWIGTYPE_p_aout_data_struct aout_data) {
        bfdwJNI.set_bfd_tdata_aout_data(swigCPtr, SWIGTYPE_p_aout_data_struct.getCPtr(aout_data));
    }

    public SWIGTYPE_p_aout_data_struct getAout_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_aout_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_aout_data_struct(cPtr, false);
    }

    public void setAout_ar_data(SWIGTYPE_p_artdata aout_ar_data) {
        bfdwJNI.set_bfd_tdata_aout_ar_data(swigCPtr, SWIGTYPE_p_artdata.getCPtr(aout_ar_data));
    }

    public SWIGTYPE_p_artdata getAout_ar_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_aout_ar_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_artdata(cPtr, false);
    }

    public void setOasys_obj_data(SWIGTYPE_p__oasys_data oasys_obj_data) {
        bfdwJNI.set_bfd_tdata_oasys_obj_data(swigCPtr, SWIGTYPE_p__oasys_data.getCPtr(oasys_obj_data));
    }

    public SWIGTYPE_p__oasys_data getOasys_obj_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_oasys_obj_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p__oasys_data(cPtr, false);
    }

    public void setOasys_ar_data(SWIGTYPE_p__oasys_ar_data oasys_ar_data) {
        bfdwJNI.set_bfd_tdata_oasys_ar_data(swigCPtr, SWIGTYPE_p__oasys_ar_data.getCPtr(oasys_ar_data));
    }

    public SWIGTYPE_p__oasys_ar_data getOasys_ar_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_oasys_ar_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p__oasys_ar_data(cPtr, false);
    }

    public void setCoff_obj_data(coff_data_type coff_obj_data) {
        bfdwJNI.set_bfd_tdata_coff_obj_data(swigCPtr, coff_data_type.getCPtr(coff_obj_data));
    }

    public coff_data_type getCoff_obj_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_coff_obj_data(swigCPtr);
        return (cPtr == 0) ? null : new coff_data_type(cPtr, false);
    }

    public void setPe_obj_data(pe_data_type pe_obj_data) {
        bfdwJNI.set_bfd_tdata_pe_obj_data(swigCPtr, pe_data_type.getCPtr(pe_obj_data));
    }

    public pe_data_type getPe_obj_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_pe_obj_data(swigCPtr);
        return (cPtr == 0) ? null : new pe_data_type(cPtr, false);
    }

    public void setXcoff_obj_data(xcoff_tdata xcoff_obj_data) {
        bfdwJNI.set_bfd_tdata_xcoff_obj_data(swigCPtr, xcoff_tdata.getCPtr(xcoff_obj_data));
    }

    public xcoff_tdata getXcoff_obj_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_xcoff_obj_data(swigCPtr);
        return (cPtr == 0) ? null : new xcoff_tdata(cPtr, false);
    }

    public void setEcoff_obj_data(SWIGTYPE_p_ecoff_tdata ecoff_obj_data) {
        bfdwJNI.set_bfd_tdata_ecoff_obj_data(swigCPtr, SWIGTYPE_p_ecoff_tdata.getCPtr(ecoff_obj_data));
    }

    public SWIGTYPE_p_ecoff_tdata getEcoff_obj_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_ecoff_obj_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_ecoff_tdata(cPtr, false);
    }

    public void setIeee_data(SWIGTYPE_p_ieee_data_struct ieee_data) {
        bfdwJNI.set_bfd_tdata_ieee_data(swigCPtr, SWIGTYPE_p_ieee_data_struct.getCPtr(ieee_data));
    }

    public SWIGTYPE_p_ieee_data_struct getIeee_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_ieee_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_ieee_data_struct(cPtr, false);
    }

    public void setIeee_ar_data(SWIGTYPE_p_ieee_ar_data_struct ieee_ar_data) {
        bfdwJNI.set_bfd_tdata_ieee_ar_data(swigCPtr, SWIGTYPE_p_ieee_ar_data_struct.getCPtr(ieee_ar_data));
    }

    public SWIGTYPE_p_ieee_ar_data_struct getIeee_ar_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_ieee_ar_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_ieee_ar_data_struct(cPtr, false);
    }

    public void setSrec_data(SWIGTYPE_p_srec_data_struct srec_data) {
        bfdwJNI.set_bfd_tdata_srec_data(swigCPtr, SWIGTYPE_p_srec_data_struct.getCPtr(srec_data));
    }

    public SWIGTYPE_p_srec_data_struct getSrec_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_srec_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_srec_data_struct(cPtr, false);
    }

    public void setIhex_data(SWIGTYPE_p_ihex_data_struct ihex_data) {
        bfdwJNI.set_bfd_tdata_ihex_data(swigCPtr, SWIGTYPE_p_ihex_data_struct.getCPtr(ihex_data));
    }

    public SWIGTYPE_p_ihex_data_struct getIhex_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_ihex_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_ihex_data_struct(cPtr, false);
    }

    public void setTekhex_data(SWIGTYPE_p_tekhex_data_struct tekhex_data) {
        bfdwJNI.set_bfd_tdata_tekhex_data(swigCPtr, SWIGTYPE_p_tekhex_data_struct.getCPtr(tekhex_data));
    }

    public SWIGTYPE_p_tekhex_data_struct getTekhex_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_tekhex_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_tekhex_data_struct(cPtr, false);
    }

    public void setElf_obj_data(elf_obj_tdata elf_obj_data) {
        bfdwJNI.set_bfd_tdata_elf_obj_data(swigCPtr, elf_obj_tdata.getCPtr(elf_obj_data));
    }

    public elf_obj_tdata getElf_obj_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_elf_obj_data(swigCPtr);
        return (cPtr == 0) ? null : new elf_obj_tdata(cPtr, false);
    }

    public void setNlm_obj_data(SWIGTYPE_p_nlm_obj_tdata nlm_obj_data) {
        bfdwJNI.set_bfd_tdata_nlm_obj_data(swigCPtr, SWIGTYPE_p_nlm_obj_tdata.getCPtr(nlm_obj_data));
    }

    public SWIGTYPE_p_nlm_obj_tdata getNlm_obj_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_nlm_obj_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_nlm_obj_tdata(cPtr, false);
    }

    public void setBout_data(SWIGTYPE_p_bout_data_struct bout_data) {
        bfdwJNI.set_bfd_tdata_bout_data(swigCPtr, SWIGTYPE_p_bout_data_struct.getCPtr(bout_data));
    }

    public SWIGTYPE_p_bout_data_struct getBout_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_bout_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_bout_data_struct(cPtr, false);
    }

    public void setMmo_data(SWIGTYPE_p_mmo_data_struct mmo_data) {
        bfdwJNI.set_bfd_tdata_mmo_data(swigCPtr, SWIGTYPE_p_mmo_data_struct.getCPtr(mmo_data));
    }

    public SWIGTYPE_p_mmo_data_struct getMmo_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_mmo_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_mmo_data_struct(cPtr, false);
    }

    public void setSun_core_data(SWIGTYPE_p_sun_core_struct sun_core_data) {
        bfdwJNI.set_bfd_tdata_sun_core_data(swigCPtr, SWIGTYPE_p_sun_core_struct.getCPtr(sun_core_data));
    }

    public SWIGTYPE_p_sun_core_struct getSun_core_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_sun_core_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_sun_core_struct(cPtr, false);
    }

    public void setSco5_core_data(SWIGTYPE_p_sco5_core_struct sco5_core_data) {
        bfdwJNI.set_bfd_tdata_sco5_core_data(swigCPtr, SWIGTYPE_p_sco5_core_struct.getCPtr(sco5_core_data));
    }

    public SWIGTYPE_p_sco5_core_struct getSco5_core_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_sco5_core_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_sco5_core_struct(cPtr, false);
    }

    public void setTrad_core_data(SWIGTYPE_p_trad_core_struct trad_core_data) {
        bfdwJNI.set_bfd_tdata_trad_core_data(swigCPtr, SWIGTYPE_p_trad_core_struct.getCPtr(trad_core_data));
    }

    public SWIGTYPE_p_trad_core_struct getTrad_core_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_trad_core_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_trad_core_struct(cPtr, false);
    }

    public void setSom_data(SWIGTYPE_p_som_data_struct som_data) {
        bfdwJNI.set_bfd_tdata_som_data(swigCPtr, SWIGTYPE_p_som_data_struct.getCPtr(som_data));
    }

    public SWIGTYPE_p_som_data_struct getSom_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_som_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_som_data_struct(cPtr, false);
    }

    public void setHpux_core_data(SWIGTYPE_p_hpux_core_struct hpux_core_data) {
        bfdwJNI.set_bfd_tdata_hpux_core_data(swigCPtr, SWIGTYPE_p_hpux_core_struct.getCPtr(hpux_core_data));
    }

    public SWIGTYPE_p_hpux_core_struct getHpux_core_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_hpux_core_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_hpux_core_struct(cPtr, false);
    }

    public void setHppabsd_core_data(SWIGTYPE_p_hppabsd_core_struct hppabsd_core_data) {
        bfdwJNI.set_bfd_tdata_hppabsd_core_data(swigCPtr, SWIGTYPE_p_hppabsd_core_struct.getCPtr(hppabsd_core_data));
    }

    public SWIGTYPE_p_hppabsd_core_struct getHppabsd_core_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_hppabsd_core_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_hppabsd_core_struct(cPtr, false);
    }

    public void setSgi_core_data(SWIGTYPE_p_sgi_core_struct sgi_core_data) {
        bfdwJNI.set_bfd_tdata_sgi_core_data(swigCPtr, SWIGTYPE_p_sgi_core_struct.getCPtr(sgi_core_data));
    }

    public SWIGTYPE_p_sgi_core_struct getSgi_core_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_sgi_core_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_sgi_core_struct(cPtr, false);
    }

    public void setLynx_core_data(SWIGTYPE_p_lynx_core_struct lynx_core_data) {
        bfdwJNI.set_bfd_tdata_lynx_core_data(swigCPtr, SWIGTYPE_p_lynx_core_struct.getCPtr(lynx_core_data));
    }

    public SWIGTYPE_p_lynx_core_struct getLynx_core_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_lynx_core_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_lynx_core_struct(cPtr, false);
    }

    public void setOsf_core_data(SWIGTYPE_p_osf_core_struct osf_core_data) {
        bfdwJNI.set_bfd_tdata_osf_core_data(swigCPtr, SWIGTYPE_p_osf_core_struct.getCPtr(osf_core_data));
    }

    public SWIGTYPE_p_osf_core_struct getOsf_core_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_osf_core_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_osf_core_struct(cPtr, false);
    }

    public void setCisco_core_data(SWIGTYPE_p_cisco_core_struct cisco_core_data) {
        bfdwJNI.set_bfd_tdata_cisco_core_data(swigCPtr, SWIGTYPE_p_cisco_core_struct.getCPtr(cisco_core_data));
    }

    public SWIGTYPE_p_cisco_core_struct getCisco_core_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_cisco_core_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_cisco_core_struct(cPtr, false);
    }

    public void setVersados_data(SWIGTYPE_p_versados_data_struct versados_data) {
        bfdwJNI.set_bfd_tdata_versados_data(swigCPtr, SWIGTYPE_p_versados_data_struct.getCPtr(versados_data));
    }

    public SWIGTYPE_p_versados_data_struct getVersados_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_versados_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_versados_data_struct(cPtr, false);
    }

    public void setNetbsd_core_data(SWIGTYPE_p_netbsd_core_struct netbsd_core_data) {
        bfdwJNI.set_bfd_tdata_netbsd_core_data(swigCPtr, SWIGTYPE_p_netbsd_core_struct.getCPtr(netbsd_core_data));
    }

    public SWIGTYPE_p_netbsd_core_struct getNetbsd_core_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_netbsd_core_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_netbsd_core_struct(cPtr, false);
    }

    public void setMach_o_data(SWIGTYPE_p_mach_o_data_struct mach_o_data) {
        bfdwJNI.set_bfd_tdata_mach_o_data(swigCPtr, SWIGTYPE_p_mach_o_data_struct.getCPtr(mach_o_data));
    }

    public SWIGTYPE_p_mach_o_data_struct getMach_o_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_mach_o_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_mach_o_data_struct(cPtr, false);
    }

    public void setMach_o_fat_data(SWIGTYPE_p_mach_o_fat_data_struct mach_o_fat_data) {
        bfdwJNI.set_bfd_tdata_mach_o_fat_data(swigCPtr, SWIGTYPE_p_mach_o_fat_data_struct.getCPtr(mach_o_fat_data));
    }

    public SWIGTYPE_p_mach_o_fat_data_struct getMach_o_fat_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_mach_o_fat_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_mach_o_fat_data_struct(cPtr, false);
    }

    public void setPef_data(SWIGTYPE_p_bfd_pef_data_struct pef_data) {
        bfdwJNI.set_bfd_tdata_pef_data(swigCPtr, SWIGTYPE_p_bfd_pef_data_struct.getCPtr(pef_data));
    }

    public SWIGTYPE_p_bfd_pef_data_struct getPef_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_pef_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_bfd_pef_data_struct(cPtr, false);
    }

    public void setPef_xlib_data(SWIGTYPE_p_bfd_pef_xlib_data_struct pef_xlib_data) {
        bfdwJNI.set_bfd_tdata_pef_xlib_data(swigCPtr, SWIGTYPE_p_bfd_pef_xlib_data_struct.getCPtr(pef_xlib_data));
    }

    public SWIGTYPE_p_bfd_pef_xlib_data_struct getPef_xlib_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_pef_xlib_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_bfd_pef_xlib_data_struct(cPtr, false);
    }

    public void setSym_data(SWIGTYPE_p_bfd_sym_data_struct sym_data) {
        bfdwJNI.set_bfd_tdata_sym_data(swigCPtr, SWIGTYPE_p_bfd_sym_data_struct.getCPtr(sym_data));
    }

    public SWIGTYPE_p_bfd_sym_data_struct getSym_data() {
        long cPtr = bfdwJNI.get_bfd_tdata_sym_data(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_bfd_sym_data_struct(cPtr, false);
    }

    public void setAny(SWIGTYPE_p_void any) {
        bfdwJNI.set_bfd_tdata_any(swigCPtr, SWIGTYPE_p_void.getCPtr(any));
    }

    public SWIGTYPE_p_void getAny() {
        long cPtr = bfdwJNI.get_bfd_tdata_any(swigCPtr);
        return (cPtr == 0) ? null : new SWIGTYPE_p_void(cPtr, false);
    }

    public bfd_tdata() {
        this(bfdwJNI.new_bfd_tdata(), true);
    }
}
