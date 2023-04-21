package cookbook.model;

import cookbook.CookbookConstants;
import static cookbook.CookbookConstants.*;
import cookbook.model.filemngr.ListInitializer;
import cookbook.model.filemngr.RecipeFileReader;
import cookbook.model.filemngr.XMLFileHandler;
import cookbook.view.display.RecipeDescriptionRepr;
import cookbook.view.display.RecipeImageRepr;
import cookbook.view.display.RecipeIngredientsRepr;
import cookbook.view.display.RecipeInstructionRepr;
import cookbook.view.management.RecipeListRepr;
import java.io.File;
import java.util.Observer;
import javax.swing.SwingWorker;

/**
 * Provides access to the cookbook data (Model in MVC architecture) and methods
 * for the several component interfaces.
 * 
 * @author Dominik Schaufelberger
 */
public class CookbookManager {

    private static CookbookManager cookbookManager = new CookbookManager();

    private ICookbookComponent[] components;

    private RecipeFileReader fileReader;

    private XMLFileHandler xmlHandler;

    private RecipeList rList;

    private RecipeDescription rDescription;

    private RecipeInstruction rInstruction;

    private RecipeIngredients rIngredients;

    private RecipeImage rImage;

    private int openRecipeID;

    /**
     * Creates a singleton object initializing the cookbook data components.
     */
    private CookbookManager() {
        File[] existingRecipes;
        this.openRecipeID = RecipeList.ID_NOT_AVAILABLE;
        this.fileReader = new RecipeFileReader(RECIPE_FOLDER);
        existingRecipes = this.fileReader.getExistingRecipes();
        this.xmlHandler = new XMLFileHandler();
        this.rList = new RecipeList(ListInitializer.initializeRecipeNames(existingRecipes), ListInitializer.initializeRecipeTypes(existingRecipes));
        this.rDescription = new RecipeDescription(EMPTY_STRING);
        this.rIngredients = new RecipeIngredients();
        this.rInstruction = new RecipeInstruction(EMPTY_STRING);
        this.rImage = new RecipeImage(CookbookConstants.EMPTY_STRING_ARRAY);
        this.components = new ICookbookComponent[4];
        insertComponent(rDescription);
        insertComponent(rIngredients);
        insertComponent(rInstruction);
        insertComponent(rImage);
    }

    /**
     * Returns the only object of this class.
     * 
     * @return
     *      singleton object, providing access to the cookbook data.
     */
    public static CookbookManager getCookbookManager() {
        return cookbookManager;
    }

    public void initialize() {
    }

    /**
     * Receives an observer objekt and registers it at the fitting observable object.
     * 
     * @param observer
     *      Observer objekt to add.
     */
    public void addObserver(Observer observer) {
        if (observer instanceof RecipeListRepr) {
            rList.addObserver(observer);
            rList.initialUpdate();
        } else if (observer instanceof RecipeDescriptionRepr) {
            rDescription.addObserver(observer);
            rDescription.initialUpdate();
        } else if (observer instanceof RecipeIngredientsRepr) {
            rIngredients.addObserver(observer);
            rIngredients.initialUpdate();
        } else if (observer instanceof RecipeInstructionRepr) {
            rInstruction.addObserver(observer);
            rInstruction.initialUpdate();
        } else if (observer instanceof RecipeImageRepr) {
            rImage.addObserver(observer);
            rImage.initialUpdate();
        }
    }

    private void insertComponent(ICookbookComponent component) {
        for (int i = 0; i <= components.length - 1; i++) {
            if (components[i] == null) {
                components[i] = component;
                break;
            }
        }
    }

    public void deleteAllContent() {
        for (ICookbookComponent component : components) {
            component.deleteContent();
        }
        xmlHandler.createXMLDocument(CookbookConstants.RECIPE);
        openRecipeID = RecipeList.ID_NOT_AVAILABLE;
    }

    private void loadAllContent() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                for (ICookbookComponent component : components) {
                    component.loadContent(xmlHandler);
                }
                return Void.class.newInstance();
            }
        };
        worker.execute();
    }

    private void saveAllContent() {
        for (ICookbookComponent component : components) {
            component.saveContent(xmlHandler);
        }
    }

    /**
     * Writes the content from the cookbook components to the right position in the XML file.
     */
    private void writeContentToFile() {
        String recipeName = rDescription.getName();
        if (rList.recipeExists(recipeName)) {
            if (!xmlHandler.xmlExists() && openRecipeID != rList.getID(recipeName)) {
                xmlHandler.loadXMLFromFile(new File(RECIPE_FOLDER + recipeName + XML_FILE_SUFFIX));
            }
            saveAllContent();
        } else {
            xmlHandler.createXMLDocument(CookbookConstants.RECIPE);
            xmlHandler.createRecipeElement(rDescription.getName(), rDescription.getType());
            xmlHandler.createMultipleIngredientElements(rIngredients.getIngredients(), rIngredients.getAmounts());
            xmlHandler.createInstructionElement(rInstruction.getInstruction());
            xmlHandler.createMultipleImageElements(rImage.getImagePaths());
        }
    }

    /**
     * Opens a XML file-representation from a file located at the machines file system.
     * 
     */
    public void loadFile() {
        xmlHandler.loadXMLFromFile(new File(CookbookConstants.RECIPE_FOLDER + rList.getSelectedRecipe() + CookbookConstants.XML_FILE_SUFFIX));
        loadAllContent();
        openRecipeID = rList.getID(rList.getSelectedRecipe());
    }

    /**
     * Save content to the XML file-representation and save it in a file into the machines file system.
     * 
     */
    public void saveFile() {
        String newName = rDescription.getName();
        RecipeType newType = rDescription.getType();
        writeContentToFile();
        if (openRecipeID != RecipeList.ID_NOT_AVAILABLE) {
            if (rList.recipeExists(newName)) {
                rList.removeRecipeFromList(newName);
                xmlHandler.writeXMLToFile(new File(CookbookConstants.RECIPE_FOLDER + newName + CookbookConstants.XML_FILE_SUFFIX));
                rList.addRecipeToList(newName, newType);
                openRecipeID = rList.getID(newName);
            } else {
                xmlHandler.renameExistingXMLFile(new File(CookbookConstants.RECIPE_FOLDER + rList.getName(openRecipeID) + CookbookConstants.XML_FILE_SUFFIX), newName);
                rList.changeRecipeInList(openRecipeID, newName, newType);
            }
        } else {
            xmlHandler.writeXMLToFile(new File(CookbookConstants.RECIPE_FOLDER + newName + CookbookConstants.XML_FILE_SUFFIX));
            if (rList.recipeExists(newName)) {
                rList.removeRecipeFromList(newName);
            }
            rList.addRecipeToList(newName, newType);
            openRecipeID = rList.getID(newName);
        }
    }

    /**
     * Invokes the deleteXMLFile method from the xmlHandler, that checks if the
     * file is a valid recipe file
     * 
     */
    public void deleteFile() {
        xmlHandler.deleteXMLFile(new File(CookbookConstants.RECIPE_FOLDER + rList.getSelectedRecipe() + CookbookConstants.XML_FILE_SUFFIX));
    }

    public int getOpenRecipeID() {
        return openRecipeID;
    }

    /**
     * Gets the description component.
     * 
     * @return
     *      description component
     */
    public RecipeDescription getRDescription() {
        return this.rDescription;
    }

    /**
     * Gets the ingredients component.
     * 
     * @return 
     *      ingredients component
     */
    public RecipeIngredients getRIngredients() {
        return this.rIngredients;
    }

    /**
     * Gets the instruction component
     * 
     * @return 
     *      instruction component
     */
    public RecipeInstruction getRInstruction() {
        return this.rInstruction;
    }

    /**
     * Gets the list component.
     * 
     * @return 
     *      list component
     */
    public RecipeList getRList() {
        return this.rList;
    }

    /**
     * Gets the image component.
     * 
     * @return 
     *      image component
     */
    public RecipeImage getRImage() {
        return this.rImage;
    }
}
