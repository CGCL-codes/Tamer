package net.sf.openforge.optimize.replace;

import java.util.*;
import java.net.*;
import net.sf.openforge.app.*;
import net.sf.openforge.app.project.Option;
import net.sf.openforge.frontend.xlim.app.XLIMEngine;
import net.sf.openforge.lim.*;
import net.sf.openforge.optimize.*;

/**
 * LibDB is a class which builds and stores the Design created for
 * each Library that has been searched.  When an instance of this
 * class is queried for a given library, the map of libraries is
 * searched to return the previously built implementation if it
 * exists.  If it does not, then it is build from its source.  This
 * class also ensures that the implementation is only ever merged with
 * another design one time.
 *
 * <p>Created: Wed Apr  9 12:29:18 2003
 *
 * @author imiller, last modified by $Author: imiller $
 * @version $Id: LibDB.java 2 2005-06-09 20:00:48Z imiller $
 */
public class LibDB {

    private static final String _RCS_ = "$Rev: 2 $";

    /** A map of String->Status for each library that has been
     * built. */
    private Map nameToStatus = new HashMap();

    /** The Status instance that we use for any library which could
     * not be found. */
    private static Status NULL_STATUS = new Status(null);

    public LibDB() {
    }

    /**
     * Retrieves the {@link Design} which is the implementation of the
     * named (fully qualified) library source.
     *
     * @param libName a fully qualified String name of the library.
     * @return a {@link Design}
     */
    public Design getDesign(String libName) {
        Status status = (Status) nameToStatus.get(libName);
        if (status == null) {
            Design design = buildDesignFor(libName);
            if (design != null) {
                status = new Status(design);
            } else {
                status = NULL_STATUS;
            }
            this.nameToStatus.put(libName, status);
        }
        return status.getDesign();
    }

    /**
     * Merges the named library Design into the specified design, or
     * does nothing if the library was already merged.
     *
     * @param libName a value of type 'String'
     * @param top a value of type 'Design'
     */
    public void mergeDesigns(String libName, Design top) {
        assert this.nameToStatus.containsKey(libName) : "Merge error: Library " + libName + " not previously fetched from database.";
        assert this.nameToStatus.get(libName) != NULL_STATUS : "Merge error: Library " + libName + " could not be instantiated.";
        Status status = (Status) this.nameToStatus.get(libName);
        if (!status.isMerged()) {
            status.mergeWith(top);
        }
    }

    /**
     * Builds a new and independent Design which represents the given
     * library (source file).
     *
     * @param library a value of type 'String'
     * @return a value of type 'Design'
     */
    private Design buildDesignFor(String library) {
        String[] sourcePath = EngineThread.getGenericJob().getLibrarySourcePath();
        LibraryResource lr = new LibraryResource(Collections.singletonList(library), sourcePath);
        URL url = lr.getResourceURL(library);
        Design design = null;
        if (url != null) {
            EngineThread.getGenericJob().info("Building library from source " + url);
            Engine oldEngine = EngineThread.getEngine();
            GenericJob oldGenJob = EngineThread.getGenericJob();
            GenericJob newGenJob = new GenericJob();
            Option option;
            Option newOption;
            for (Iterator it = oldGenJob.optionsMap.values().iterator(); it.hasNext(); ) {
                option = (Option) it.next();
                newOption = newGenJob.getOption(option.getOptionKey());
                newOption.replaceValue(CodeLabel.UNSCOPED, option.getValue(CodeLabel.UNSCOPED));
            }
            option = newGenJob.getOption(OptionRegistry.NO_BLOCK_IO);
            option.replaceValue(CodeLabel.UNSCOPED, "true");
            option = newGenJob.getOption(OptionRegistry.ENTRY);
            option.replaceValue(CodeLabel.UNSCOPED, "");
            option = newGenJob.getOption(OptionRegistry.OPERATOR_REPLACEMENT_MAX_LEVEL);
            option.replaceValue(CodeLabel.UNSCOPED, new Integer(0));
            try {
                option = newGenJob.getOption(OptionRegistry.TARGET);
                option.replaceValue(CodeLabel.UNSCOPED, url.getFile());
            } catch (NewJob.InvalidOptionValueException e) {
                oldGenJob.error("Library files must have the expected source language extension (ie: .c)");
                oldGenJob.error("Library file " + url.getFile() + " does not meet this requirement and can not be used.");
                return null;
            }
            option = newGenJob.getOption(OptionRegistry.ENTRY);
            option.replaceValue(CodeLabel.UNSCOPED, ":-");
            Engine libEngine = null;
            if (url.getFile().endsWith(".xlim")) {
                libEngine = new XLIMEngine(newGenJob);
            } else {
                oldGenJob.error("Unknown library file extension " + url);
            }
            design = libEngine.buildLim();
            Optimizer opts = new Optimizer();
            opts.optimize(design);
            libEngine.kill();
            oldEngine.updateJobThread();
        } else {
            EngineThread.getGenericJob().warn("Library source file " + library + " not found");
        }
        return design;
    }

    /**
     * A convenience class for tracking the library design and whether
     * it has been merged yet.
     */
    private static class Status {

        private Design libDesign = null;

        private boolean addedToDesign = false;

        public Status(Design libDesign) {
            this.libDesign = libDesign;
        }

        public Design getDesign() {
            return this.libDesign;
        }

        public void mergeWith(Design design) {
            assert !isMerged() : "Already merged!";
            if (!isMerged()) {
                design.mergeResources(this.libDesign);
                this.addedToDesign = true;
            }
        }

        public boolean isMerged() {
            return this.addedToDesign;
        }
    }
}
