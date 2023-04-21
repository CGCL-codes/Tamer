package it.diamonds.mocks;

import it.diamonds.gems.Gem;
import it.diamonds.gems.Droppable;
import it.diamonds.gems.DroppableGenerator;

public class MockDroppableGenerator implements DroppableGenerator {

    public MockDroppableGenerator() {
    }

    public Droppable extract() {
        return Gem.createForTesting();
    }

    public Droppable getGemAt(int index) {
        return Gem.createForTesting();
    }
}
