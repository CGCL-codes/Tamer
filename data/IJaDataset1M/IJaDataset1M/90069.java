package electrode;

/**
 * Encapsulates configuration stored in the Electrode manifest file.
 * 
 * @author cgranade
 */
public class Manifest {

    /**
     * Returns information about the game described by this manifest.
     * 
     * @return Object encapsulating the game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the information about the game described by this manifest.
     * @param game Object encapsulating the game.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    public static class Game {

        private String init, titleScreen;

        /**
         * Gets the name of the file which should be run to initialize
         * the game state.
         * 
         * @return Name of the initializer script.
         */
        public String getInit() {
            return init;
        }

        /**
         * Sets the name of the file which should be run to initialize
         * the game state.
         * 
         * @param init Name of the initializer script.
         */
        public void setInit(String init) {
            this.init = init;
        }

        /**
         * Gets the name of the script responsible for executing the game's
         * title screen. This script should be responsible for transitioning to
         * whatever new state is appropriate.
         * 
         * @return Name of title screen script.
         */
        public String getTitleScreen() {
            return titleScreen;
        }

        /**
         * 
         * Sets the name of the script responsible for executing the game's
         * title screen. This script should be responsible for transitioning to
         * whatever new state is appropriate.
         * 
         * @param titleScreen Name of title screen script.
         */
        public void setTitleScreen(String titleScreen) {
            this.titleScreen = titleScreen;
        }
    }

    private String name;

    private Version version;

    private Module[] requires;

    private String[] authors;

    private String license;

    private Game game;

    public Manifest() {
    }

    /**
     * Constructs a new manifest object, given the name and version of a game.
     * 
     * @param name Name of the game described by the new manifest.
     * @param version Version of the game described by the new manifest.
     */
    public Manifest(String name, Version version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public Version getVersion() {
        return version;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    /**
     * Gets a list of modules required by the game described by this manifest.
     * 
     * @return List of required modules.
     */
    public Module[] getRequires() {
        return requires;
    }

    public void setRequires(Module[] requires) {
        this.requires = requires;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
