package net.sf.fallfair.prize;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.fallfair.CRUD.AbstractIBatisCRUD;
import net.sf.fallfair.context.FairContext;
import net.sf.fallfair.section.Section;

public class PrizeIBatisCRUD extends AbstractIBatisCRUD implements PrizeCRUD {

    public static final String PLACE_KEY = "PLACE";

    public static final String DESCRIPTION_KEY = "DESCRIPTION";

    public static final String AMOUNT_KEY = "AMOUNT";

    public static final String EXHIBITOR_ID_KEY = "EXHIBITOR_ID";

    public static final String SECTION_ID_KEY = "SECTION_ID";

    public static final String PRIZE_IDS_KEY = "PRIZE_IDS";

    public static final String READ_BY_ID = "Prize.readById";

    public static final String INSERT = "Prize.insert";

    public static final String UPDATE = "Prize.update";

    public static final String CLEAR = "Prize.clear";

    public static final String CLEAR_INVALID_PRIZES = "Prize.clearInvalidPrizes";

    @Override
    public void clear(Section section, FairContext context) throws SQLException {
        Map<String, Object> prizeListValues = new HashMap<String, Object>();
        prizeListValues.put(ID_KEY, section.getId());
        prizeListValues.put(YEAR_KEY, section.getYear());
        getSQLMapInstance().update(CLEAR, prizeListValues);
    }

    @Override
    public void update(Section section, FairContext context) throws SQLException {
        List<Integer> validPrizeIds = new ArrayList<Integer>();
        Map<String, Object> prizeListValues = new HashMap<String, Object>();
        prizeListValues.put(SECTION_ID_KEY, section.getId());
        prizeListValues.put(YEAR_KEY, section.getYear());
        for (Prize prize : section.getPrizes()) {
            prizeListValues.put(ID_KEY, prize.getId());
            prizeListValues.put(PLACE_KEY, prize.getPlace());
            prizeListValues.put(DESCRIPTION_KEY, prize.getDescription());
            prizeListValues.put(AMOUNT_KEY, prize.getAmount());
            if (null != prize.getExhibitor()) {
                prizeListValues.put(EXHIBITOR_ID_KEY, prize.getExhibitor().getId());
            } else {
                prizeListValues.put(EXHIBITOR_ID_KEY, null);
            }
            prizeListValues.put(SECTION_ID_KEY, section.getId());
            Map<String, Object> prizeListValue = new HashMap<String, Object>();
            prizeListValue.put(ID_KEY, prize.getId());
            prizeListValue.put(YEAR_KEY, section.getYear());
            if (null == readById(prize.getId(), context)) {
                LOGGER.debug("INSERT " + section.getId() + " " + prize.getId() + " " + prize.getPlace() + " " + (null == prize.getExhibitor() ? "" : prize.getExhibitor().getId()));
                int insertResult = ((Integer) getSQLMapInstance().insert(INSERT, prizeListValues)).intValue();
                if (0 == prize.getId()) {
                    prize.setId(insertResult);
                }
            } else {
                LOGGER.debug("UPDATE " + section.getId() + " " + prize.getId() + " " + prize.getPlace() + " " + (null == prize.getExhibitor() ? "" : prize.getExhibitor().getId()));
                getSQLMapInstance().update(UPDATE, prizeListValues);
            }
            validPrizeIds.add(Integer.valueOf(prize.getId()));
        }
        prizeListValues = new HashMap<String, Object>();
        prizeListValues.put(SECTION_ID_KEY, section.getId());
        prizeListValues.put(YEAR_KEY, section.getYear());
        prizeListValues.put(PRIZE_IDS_KEY, validPrizeIds);
        if (0 != validPrizeIds.size()) {
            if (LOGGER.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder();
                for (Integer id : validPrizeIds) {
                    if (0 != sb.length()) {
                        sb.append(", ");
                    }
                    sb.append(id);
                }
                LOGGER.debug("CLEAR INVALID PRIZES " + sb.toString());
            }
            getSQLMapInstance().update(CLEAR_INVALID_PRIZES, prizeListValues);
        }
    }

    @Override
    public Prize readById(int id, FairContext context) throws SQLException {
        Map<String, Object> searchCriteria = new HashMap<String, Object>();
        searchCriteria.put(ID_KEY, id);
        searchCriteria.put(YEAR_KEY, context.getYear());
        return (Prize) getSQLMapInstance().queryForObject(READ_BY_ID, searchCriteria);
    }

    @Override
    public void update(Prize obj, FairContext context) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Prize obj, FairContext context) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Prize> getAll(FairContext context) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
