package com.mdt.sync;

import org.apache.log4j.Logger;

/**
 * 
 * @author Will Ross Jun 26, 2007
 */
public class CopyItemCommand implements Command {

    private static final Logger log = Logger.getLogger(CopyItemCommand.class);

    final Item source;

    final Category targetCategory;

    final Repository targetRepo;

    public CopyItemCommand(final Item source, final Category targetCategory, final Repository targetRepo) {
        this.source = source;
        this.targetCategory = targetCategory;
        this.targetRepo = targetRepo;
    }

    public void execute() {
        if (log.isDebugEnabled()) log.debug("Copying item " + source + " to category " + targetCategory + " in repository " + targetRepo);
        targetRepo.createCopy(source, targetCategory);
    }

    public void simulate() {
        if (log.isDebugEnabled()) log.debug("Simulation :: Copying item " + source + " to category " + targetCategory + " in repository " + targetRepo);
    }

    @Override
    public String toString() {
        return "Copy " + source + " to " + targetCategory + " in " + targetRepo;
    }
}
