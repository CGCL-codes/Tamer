package net.sourceforge.mpango.directory.factory;

import net.sourceforge.mpango.dto.CellDTO;
import net.sourceforge.mpango.entity.Cell;

public class CellFactory extends BaseFactory<CellDTO, Cell> {

    private CellFactory() {
        super();
    }

    /**
	 * returns new instance of factory
	 * 
	 * @return
	 */
    public static CellFactory instance() {
        return new CellFactory();
    }

    public Cell create(CellDTO dto) {
        Cell cell = new Cell(dto.getRow(), dto.getColumn());
        cell.setColumn(dto.getColumn());
        cell.setRow(dto.getRow());
        cell.setIdentifier(dto.getId());
        cell.setAttackBonus(dto.getAttackBonus());
        cell.setConstructions(ConstructionFactory.instance().createList(dto.getConstructions()));
        cell.setDefenseBonus(dto.getDefenseBonus());
        return cell;
    }
}
