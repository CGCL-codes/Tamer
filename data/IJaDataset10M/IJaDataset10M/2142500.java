package net.sourceforge.solexatools.business.impl;

import net.sourceforge.solexatools.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sourceforge.solexatools.business.SampleService;
import net.sourceforge.solexatools.dao.SampleDAO;
import net.sourceforge.solexatools.model.Experiment;
import net.sourceforge.solexatools.model.ExperimentType;
import net.sourceforge.solexatools.model.Organism;
import net.sourceforge.solexatools.model.Sample;
import net.sourceforge.solexatools.model.Registration;

public class SampleServiceImpl implements SampleService {

    private SampleDAO sampleDAO = null;

    private static final Log log = LogFactory.getLog(SampleServiceImpl.class);

    public SampleServiceImpl() {
        super();
    }

    /**
	 * Sets a private member variable with an instance of
	 * an implementation of SampleDAO. This method
	 * is called by the Spring framework at run time.
	 *
	 * @param		sampleDAO implementation of SampleDAO
	 * @see			SampleDAO
	 */
    public void setSampleDAO(SampleDAO sampleDAO) {
        this.sampleDAO = sampleDAO;
    }

    /**
	 * Inserts an instance of Sample into the database.
	 *
	 * @param sampleDAO instance of SampleDAO
	 */
    public void insert(Sample sample) {
        sample.setCreateTimestamp(new Date());
        sampleDAO.insert(sample);
    }

    /**
	 * Updates an instance of Sample in the database.
	 *
	 * @param sample instance of Sample
	 */
    public void update(Sample sample) {
        sampleDAO.update(sample);
    }

    public void refresh(Sample sample) {
        sampleDAO.refresh(sample);
    }

    public Sample findByName(String name) {
        Sample sample = null;
        if (name != null) {
            try {
                sample = sampleDAO.findByName(name.trim().toLowerCase());
            } catch (Exception exception) {
                log.debug("Cannot find Sample by name " + name);
            }
        }
        return sample;
    }

    public boolean hasNameBeenUsed(String oldName, String newName) {
        boolean nameUsed = false;
        boolean checkName = true;
        if (newName != null) {
            if (oldName != null) {
                checkName = !newName.trim().equalsIgnoreCase(oldName.trim());
            }
            if (checkName) {
                Sample sample = this.findByName(newName.trim().toLowerCase());
                if (sample != null) {
                    nameUsed = true;
                }
            }
        }
        return nameUsed;
    }

    public List<Organism> listOrganisms() {
        return sampleDAO.listOrganisms();
    }

    public List<ExperimentType> listExperimentTypes() {
        return sampleDAO.listExperimentTypes();
    }
}
