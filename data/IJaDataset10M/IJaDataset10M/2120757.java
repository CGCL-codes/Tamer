package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of struct "TagFTGroupTaggedComponent"
 *	@author JacORB IDL compiler 
 */
public final class TagFTGroupTaggedComponent implements org.omg.CORBA.portable.IDLEntity {

    public TagFTGroupTaggedComponent() {
    }

    public org.omg.GIOP.Version version;

    public java.lang.String ft_domain_id;

    public long object_group_id;

    public int object_group_ref_version;

    public TagFTGroupTaggedComponent(org.omg.GIOP.Version version, java.lang.String ft_domain_id, long object_group_id, int object_group_ref_version) {
        this.version = version;
        this.ft_domain_id = ft_domain_id;
        this.object_group_id = object_group_id;
        this.object_group_ref_version = object_group_ref_version;
    }
}
