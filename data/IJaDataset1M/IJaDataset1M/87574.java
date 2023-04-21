package org.openscience.jchempaint.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.JCheckBoxMenuItem;
import org.openscience.cdk.dict.DictionaryDatabase;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.tools.manipulator.ChemModelManipulator;
import org.openscience.cdk.validate.BasicValidator;
import org.openscience.cdk.validate.CDKValidator;
import org.openscience.cdk.validate.DictionaryValidator;
import org.openscience.cdk.validate.ProblemMarker;
import org.openscience.cdk.validate.ValencyValidator;
import org.openscience.cdk.validate.ValidatorEngine;
import org.openscience.jchempaint.JCPPropertyHandler;
import org.openscience.jchempaint.dialog.ValidateFrame;

/**
 * An action opening a validation frame
 * 
 * @cdk.module jchempaint
 * @author     E.L. Willighagen <elw38@cam.ac.uk>
 */
public class ValidateAction extends JCPAction {

    private static final long serialVersionUID = -3776589605934024224L;

    private static ValidatorEngine engine;

    private static DictionaryDatabase dictdb;

    ValidateFrame frame = null;

    public void actionPerformed(ActionEvent event) {
        logger.debug("detected validate action: ", type);
        if (type.equals("run")) {
            IChemObject object = getSource(event);
            if (object == null) {
                org.openscience.cdk.interfaces.IChemModel model = jcpPanel.getChemModel();
                if (model != null) {
                    runValidate(model);
                } else {
                    System.out.println("Empty model");
                }
            } else {
                logger.debug("Validate called from popup menu!");
                runValidate(object);
            }
        } else if (type.equals("clear")) {
            clearValidate();
        } else if (type.startsWith("toggle") && type.length() > 6) {
            String toggle = type.substring(6);
            try {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) event.getSource();
                boolean newChecked = !menuItem.isSelected();
                menuItem.setSelected(newChecked);
                if (toggle.equals("Basic")) {
                    if (newChecked) {
                        logger.info("Turned on " + toggle);
                        getValidatorEngine().addValidator(new BasicValidator());
                    } else {
                        logger.info("Turned off " + toggle);
                        getValidatorEngine().removeValidator(new BasicValidator());
                    }
                } else if (toggle.equals("CDK")) {
                    if (newChecked) {
                        logger.info("Turned on " + toggle);
                        getValidatorEngine().addValidator(new CDKValidator());
                    } else {
                        logger.info("Turned off " + toggle);
                        getValidatorEngine().removeValidator(new CDKValidator());
                    }
                } else {
                    logger.error("Don't know what to toggle: " + toggle);
                }
            } catch (ClassCastException exception) {
                logger.error("Cannot toggle a non JCheckBoxMenuItem!");
            }
        } else {
            logger.error("Unknown command: " + type);
        }
    }

    private void clearValidate() {
        org.openscience.cdk.interfaces.IChemModel model = jcpPanel.getChemModel();
        Iterator containers = ChemModelManipulator.getAllAtomContainers(model).iterator();
        while (containers.hasNext()) {
            IAtomContainer atoms = (IAtomContainer) containers.next();
            logger.info("Clearing errors on atoms: " + atoms.getAtomCount());
            for (int i = 0; i < atoms.getAtomCount(); i++) {
                ProblemMarker.unmark(atoms.getAtom(i));
            }
        }
        jcpPanel.get2DHub().updateView();
    }

    private void runValidate(IChemObject object) {
        logger.info("Running validation");
        clearValidate();
        if (jcpPanel.getChemModel() != null) {
            frame = new ValidateFrame(jcpPanel);
            frame.validate(object);
            frame.pack();
            frame.setVisible(true);
        }
    }

    /**
	 *  Gets the validatorEngine attribute of the JChemPaintEditorPanel class
	 *
	 *@return    The validatorEngine value
	 */
    public static ValidatorEngine getValidatorEngine() {
        if (engine == null) {
            engine = new ValidatorEngine();
            engine.addValidator(new BasicValidator());
            engine.addValidator(new ValencyValidator());
            engine.addValidator(new CDKValidator());
            engine.addValidator(new DictionaryValidator(getDictionaryDatabase()));
        }
        return engine;
    }

    /**
	 *  Gets the dictionaryDatabase attribute of the JChemPaint object
	 *
	 *@return    The dictionaryDatabase value
	 */
    public static DictionaryDatabase getDictionaryDatabase() {
        if (dictdb == null) {
            dictdb = new DictionaryDatabase();
            try {
                File dictdir = new File(JCPPropertyHandler.getInstance().getJChemPaintDir(), "dicts");
                logger.info("User dict dir: ", dictdir);
                logger.debug("       exists: ", dictdir.exists());
                logger.debug("  isDirectory: ", dictdir.isDirectory());
                if (dictdir.exists() && dictdir.isDirectory()) {
                    File[] dicts = dictdir.listFiles();
                    for (int i = 0; i < dicts.length; i++) {
                        try {
                            FileReader reader = new FileReader(dicts[i]);
                            String filename = dicts[i].getName();
                            dictdb.readDictionary(reader, filename.substring(0, filename.indexOf('.')));
                        } catch (IOException exception) {
                            logger.error("Problem with reading macie dictionary...");
                        }
                    }
                }
                logger.info("Read these dictionaries: ");
                Enumeration dicts = dictdb.listDictionaries();
                while (dicts.hasMoreElements()) {
                    logger.info(" - ", dicts.nextElement().toString());
                }
            } catch (Exception exc) {
                logger.error("Could not handle dictionary initialization. Maybe I'm running in a sandbox.");
            }
        }
        return dictdb;
    }
}
