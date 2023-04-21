package com.eteks.sweethome3d.viewcontroller;

import com.eteks.sweethome3d.model.BackgroundImage;
import com.eteks.sweethome3d.model.CatalogPieceOfFurniture;
import com.eteks.sweethome3d.model.CatalogTexture;
import com.eteks.sweethome3d.model.FurnitureCatalog;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.UserPreferences;

/**
 * A factory that specifies how to create the views displayed in Sweet Home 3D. 
 * @author Emmanuel Puybaret
 */
public interface ViewFactory {

    /**
   * Returns a new view that displays furniture <code>catalog</code>.
   */
    public abstract View createFurnitureCatalogView(FurnitureCatalog catalog, UserPreferences preferences, FurnitureCatalogController furnitureCatalogController);

    /**
   * Returns a new view that displays <code>home</code> furniture list.
   */
    public abstract View createFurnitureView(Home home, UserPreferences preferences, FurnitureController furnitureController);

    /**
   * Returns a new view that displays <code>home</code> on a plan.
   */
    public abstract PlanView createPlanView(Home home, UserPreferences preferences, PlanController planController);

    /**
   * Returns a new view that displays <code>home</code> in 3D.
   */
    public abstract View createView3D(Home home, UserPreferences preferences, HomeController3D homeController3D);

    /**
   * Returns a new view that displays <code>home</code> and its sub views.
   */
    public abstract HomeView createHomeView(Home home, UserPreferences preferences, HomeController homeController);

    /**
   * Returns a new view that displays a wizard. 
   */
    public abstract DialogView createWizardView(UserPreferences preferences, WizardController wizardController);

    /**
   * Returns a new view that displays the different steps that helps the user to choose a background image. 
   */
    public abstract View createBackgroundImageWizardStepsView(BackgroundImage backgroundImage, UserPreferences preferences, BackgroundImageWizardController backgroundImageWizardController);

    /**
   * Returns a new view that displays the different steps that helps the user to import furniture. 
   */
    public abstract ImportedFurnitureWizardStepsView createImportedFurnitureWizardStepsView(CatalogPieceOfFurniture piece, String modelName, boolean importHomePiece, UserPreferences preferences, ImportedFurnitureWizardController importedFurnitureWizardController);

    /**
   * Returns a new view that displays the different steps that helps the user to import a texture. 
   */
    public abstract View createImportedTextureWizardStepsView(CatalogTexture texture, String textureName, UserPreferences preferences, ImportedTextureWizardController importedTextureWizardController);

    /**
   * Returns a new view that displays message for a threaded task.
   */
    public abstract ThreadedTaskView createThreadedTaskView(String taskMessage, UserPreferences userPreferences, ThreadedTaskController threadedTaskController);

    /**
   * Returns a new view that edits user preferences.
   */
    public abstract DialogView createUserPreferencesView(UserPreferences preferences, UserPreferencesController userPreferencesController);

    /**
   * Returns a new view that edits furniture values.
   */
    public abstract DialogView createHomeFurnitureView(UserPreferences preferences, HomeFurnitureController homeFurnitureController);

    /**
   * Returns a new view that edits wall values.
   */
    public abstract DialogView createWallView(UserPreferences preferences, WallController wallController);

    /**
   * Returns a new view that edits room values.
   */
    public abstract DialogView createRoomView(UserPreferences preferences, RoomController roomController);

    /**
   * Returns a new view that edits label values.
   */
    public abstract DialogView createLabelView(boolean modification, UserPreferences preferences, LabelController labelController);

    /**
   * Returns a new view that edits 3D attributes.
   */
    public abstract DialogView createHome3DAttributesView(UserPreferences preferences, Home3DAttributesController home3DAttributesController);

    /**
   * Returns a new view that edits the texture of its controller.  
   */
    public abstract TextureChoiceView createTextureChoiceView(UserPreferences preferences, TextureChoiceController textureChoiceController);

    /**
   * Creates a new view that edits page setup.
   */
    public abstract DialogView createPageSetupView(UserPreferences preferences, PageSetupController pageSetupController);

    /**
   * Returns a new view that displays home print preview. 
   */
    public abstract DialogView createPrintPreviewView(Home home, UserPreferences preferences, HomeController homeController, PrintPreviewController printPreviewController);

    /**
   * Returns a new view able to compute a photo realistic image of a home. 
   */
    public abstract DialogView createPhotoView(Home home, UserPreferences preferences, PhotoController photoController);

    /**
   * Returns a new view able to compute a 3D video of a home. 
   */
    public abstract DialogView createVideoView(Home home, UserPreferences preferences, VideoController videoController);

    /**
   * Returns a new view that displays Sweet Home 3D help.
   */
    public abstract HelpView createHelpView(UserPreferences preferences, HelpController helpController);
}
