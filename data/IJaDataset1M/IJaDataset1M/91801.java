package beans;

public class Recipe {

    private int id;

    private String name;

    private IngredientContainer ingredients;

    private String instructions;

    private int categoryId;

    /**
	 * Constructor
	 * */
    public Recipe() {
    }

    /**
	 * Constructor
	 * @param int id
	 * @param String newHeader
	 * @param IngredientContainer newIngredients
	 * @param String newInstructions@param
     * @param int categoryId
	 * */
    public Recipe(int id, String newName, IngredientContainer newIngredients, String newInstructions, int categoryId) {
        setId(id);
        setName(newName);
        setIngredients(newIngredients);
        setInstructions(newInstructions);
        setCategoryId(categoryId);
    }

    /**
	 * 
	 */
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
	 * 
	 */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * 
	 */
    public void setIngredients(IngredientContainer ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientContainer getIngredients() {
        return this.ingredients;
    }

    /**
	 * 
	 */
    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * 
     */
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int newCategoryId) {
        categoryId = newCategoryId;
    }
}
