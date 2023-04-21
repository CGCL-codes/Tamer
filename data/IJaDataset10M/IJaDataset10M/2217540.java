package tafat.metamodel.recipe;

import java.util.ArrayList;
import tafat.engine.social.Recipe;
import tafat.metamodel.entity.Lighting;
import tafat.metamodel.entity.WashingMachine;

public class UseWashingMachineRecipe extends Recipe {

    public UseWashingMachineRecipe(int recipeNumber) {
        if (recipeNumber == 0) {
            Class<?> class1 = WashingMachine.class;
            int[] startTime1 = { 0, 0 };
            int[] durationTime1 = { -1, -1 };
            String specialHandling1 = "ON40";
            RecipeLine line1 = new RecipeLine(class1, startTime1, durationTime1, specialHandling1);
            recipe = new ArrayList<RecipeLine>();
            recipe.add(line1);
            synchronization = false;
            return;
        }
        if (recipeNumber == 1) {
            Class<?> class1 = WashingMachine.class;
            int[] startTime1 = { 0, 0 };
            int[] durationTime1 = { -1, -1 };
            String specialHandling1 = "ON60";
            RecipeLine line1 = new RecipeLine(class1, startTime1, durationTime1, specialHandling1);
            recipe = new ArrayList<RecipeLine>();
            recipe.add(line1);
            synchronization = false;
            return;
        }
        if (recipeNumber == 2) {
            Class<?> class1 = WashingMachine.class;
            int[] startTime1 = { 0, 0 };
            int[] durationTime1 = { -1, -1 };
            String specialHandling1 = "ON95";
            RecipeLine line1 = new RecipeLine(class1, startTime1, durationTime1, specialHandling1);
            recipe = new ArrayList<RecipeLine>();
            recipe.add(line1);
            synchronization = false;
            return;
        }
        if (recipeNumber == 3) {
            Class<?> class1 = Lighting.class;
            int[] startTime1 = { 0, 0 };
            int[] durationTime1 = { 90, 120 };
            double[] specialHandling1 = { 0, 0 };
            RecipeLine line1 = new RecipeLine(class1, startTime1, durationTime1, specialHandling1);
            Class<?> class2 = Lighting.class;
            int[] startTime2 = { -1, -1 };
            int[] durationTime2 = { -1, -1 };
            double[] specialHandling2 = { 0.3, 0.5 };
            RecipeLine line2 = new RecipeLine(class2, startTime2, durationTime2, specialHandling2);
            recipe = new ArrayList<RecipeLine>();
            recipe.add(line1);
            recipe.add(line2);
            synchronization = false;
            return;
        }
    }
}
