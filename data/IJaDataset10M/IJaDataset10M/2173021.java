package net.davidrobles.games.pacsim.agents;

import net.davidrobles.games.pacsim.model.ModelInterface;
import net.davidrobles.games.pacsim.maze.Direction;

public interface PacManAgent {

    Direction direction(ModelInterface model);
}
