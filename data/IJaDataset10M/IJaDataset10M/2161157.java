package net.sf.adatagenerator.ex.cdc1.bean;

import java.util.Properties;
import net.sf.adatagenerator.api.CreationException;
import net.sf.adatagenerator.api.Template;
import net.sf.adatagenerator.core.TemplateProperties;
import net.sf.adatagenerator.ex.cdc1.api.Cdc1Record;
import net.sf.adatagenerator.ex.cdc1.api.ExtendedCdc1Record;
import net.sf.adatagenerator.ex.cdc1.api.SiblingRole;
import com.choicemaker.shared.util.CMEqual;
import com.choicemaker.shared.util.CMHashUtils;

public class SynthesizedCdc1RecordBean extends Cdc1RecordBean implements ExtendedCdc1Record, Template<ExtendedCdc1Record> {

    private static final long serialVersionUID = 1L;

    protected static final boolean ARE_NULLS_EQUAL = true;

    private final TemplateProperties generationProperties = new TemplateProperties();

    private String patientId;

    private String recordId;

    private Enum<SiblingRole> siblingRole;

    /**
	 * Constructs a bean with all null fields.
	 */
    public SynthesizedCdc1RecordBean() {
        this(new Cdc1RecordBean());
    }

    /** Partial copy constructor */
    public SynthesizedCdc1RecordBean(Cdc1Record p) {
        super(p);
    }

    /** Partial copy constructor */
    public SynthesizedCdc1RecordBean(ExtendedCdc1Record p) {
        this((Cdc1Record) p);
        if (p != null) {
            this.setPatientId(p.getPatientId());
            this.setRecordId(p.getRecordId());
            this.setSiblingRole(p.getSiblingRole());
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new SynthesizedCdc1RecordBean(this);
    }

    @Override
    public Template<ExtendedCdc1Record> cloneSynthesized() throws CreationException {
        SynthesizedCdc1RecordBean retVal;
        try {
            retVal = (SynthesizedCdc1RecordBean) clone();
        } catch (CloneNotSupportedException e) {
            throw new CreationException("unable to create clone: " + e.toString(), e);
        }
        return retVal;
    }

    protected TemplateProperties getGenerationBeanProperties() {
        return generationProperties;
    }

    @Override
    public String getPatientId() {
        return this.patientId;
    }

    @Override
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    @Override
    public String getRecordId() {
        return this.recordId;
    }

    @Override
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    @Override
    public Enum<SiblingRole> getSiblingRole() {
        return this.siblingRole;
    }

    @Override
    public void setSiblingRole(Enum<SiblingRole> role) {
        this.siblingRole = role;
    }

    @Override
    public boolean equals(Object o) {
        final boolean F = ARE_NULLS_EQUAL;
        boolean retVal = this == o ? true : false;
        if (!retVal && o != null && this.getClass() == o.getClass()) {
            SynthesizedCdc1RecordBean gp = (SynthesizedCdc1RecordBean) o;
            retVal = super.equals(o);
            retVal = retVal && CMEqual.and(getPatientId(), gp.getPatientId(), F);
            retVal = retVal && CMEqual.and(getRecordId(), gp.getRecordId(), F);
            retVal = retVal && CMEqual.and(getSiblingRole(), gp.getSiblingRole(), F);
            retVal = retVal && CMEqual.and(getProperties(), gp.getProperties(), F);
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int retVal = super.hashCode();
        retVal = CMHashUtils.hashCode(retVal, generationProperties);
        retVal = CMHashUtils.hashCode(retVal, patientId);
        retVal = CMHashUtils.hashCode(retVal, recordId);
        retVal = CMHashUtils.hashCode(retVal, siblingRole);
        return retVal;
    }

    @Override
    public Properties getProperties() {
        return getGenerationBeanProperties().getProperties();
    }

    @Override
    public void setProperty(String pn, String pv) {
        getGenerationBeanProperties().setProperty(pn, pv);
    }

    @Override
    public void removeProperty(String pn) {
        getGenerationBeanProperties().removeProperty(pn);
    }

    @Override
    public String getProperty(String pn) {
        return getGenerationBeanProperties().getProperty(pn);
    }

    @Override
    public ExtendedCdc1Record cast() {
        return this;
    }
}
