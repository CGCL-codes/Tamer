package civquest.map.vegGen;

import civquest.map.MapData;
import civquest.parser.ruleset.exception.RulesetException;

public interface VegetationGenerator {

    public void generate(MapData mapdate) throws RulesetException;
}
