package org.blueoxygen.cimande.thin.moduledescriptor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.blueoxygen.cimande.thin.DefaultPersistent;
import org.blueoxygen.cimande.thin.descriptor.Descriptor;
import org.blueoxygen.cimande.thin.module.Module;

@Entity()
@Table(name = "thin_module_descriptor")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ModuleDescriptor extends DefaultPersistent {

    private Descriptor descriptor;

    private Module module;

    @ManyToOne()
    @Column(name = "descriptor_id")
    public Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    @ManyToOne()
    @Column(name = "module_id")
    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
