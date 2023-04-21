package edu.unc.med.lccc.tcgasra.hibernateobj;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Lane entity. @author MyEclipse Persistence Tools
 */
public class Lane extends AbstractLane implements java.io.Serializable {

    /** default constructor */
    public Lane() {
    }

    /** minimal constructor */
    public Lane(Integer laneId, Timestamp createTstmp) {
        super(laneId, createTstmp);
    }

    /** full constructor */
    public Lane(Integer laneId, LibrarySelection librarySelection, LaneType laneType, Organism organism, LibraryStrategy libraryStrategy, LibrarySource librarySource, StudyType studyType, Sample sample, SequencerRun sequencerRun, Integer sequencerRunId, Registration registration, String name, String alias, String description, Integer laneIndex, String cycleDescriptor, Integer cycleCount, String cycleSequence, Boolean skip, String tags, String regions, Integer swAccession, Timestamp createTstmp, Timestamp updateTstmp, Set processingLaneses, Set iuses) {
        super(laneId, librarySelection, laneType, organism, libraryStrategy, librarySource, studyType, sample, sequencerRun, sequencerRunId, registration, name, alias, description, laneIndex, cycleDescriptor, cycleCount, cycleSequence, skip, tags, regions, swAccession, createTstmp, updateTstmp, processingLaneses, iuses);
    }
}
