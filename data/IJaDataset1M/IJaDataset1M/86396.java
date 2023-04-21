package games.strategy.triplea.ui.screen;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.data.Territory;
import games.strategy.engine.data.Unit;
import games.strategy.engine.data.UnitType;
import games.strategy.triplea.Properties;
import games.strategy.triplea.TripleAUnit;
import games.strategy.triplea.attatchments.TerritoryAttachment;
import games.strategy.triplea.attatchments.UnitAttachment;
import games.strategy.triplea.delegate.Matches;
import games.strategy.triplea.formatter.MyFormatter;
import games.strategy.triplea.image.MapImage;
import games.strategy.triplea.ui.MapData;
import games.strategy.triplea.ui.UIContext;
import games.strategy.util.CompositeMatch;
import games.strategy.util.CompositeMatchAnd;
import games.strategy.util.Match;
import games.strategy.util.Tuple;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.List;

public class UnitsDrawer implements IDrawable {

    private final int m_count;

    private final String m_unitType;

    private final String m_playerName;

    private final Point m_placementPoint;

    private final boolean m_damaged;

    private final boolean m_disabled;

    private final boolean m_overflow;

    private final String m_territoryName;

    private final UIContext m_uiContext;

    public UnitsDrawer(final int count, final String unitType, final String playerName, final Point placementPoint, final boolean damaged, final boolean disabled, final boolean overflow, final String territoryName, final UIContext uiContext) {
        m_count = count;
        m_unitType = unitType;
        m_playerName = playerName;
        m_placementPoint = placementPoint;
        m_damaged = damaged;
        m_disabled = disabled;
        m_overflow = overflow;
        m_territoryName = territoryName;
        m_uiContext = uiContext;
    }

    public void prepare() {
    }

    public Point getPlacementPoint() {
        return m_placementPoint;
    }

    public String getPlayer() {
        return m_playerName;
    }

    public void draw(final Rectangle bounds, final GameData data, final Graphics2D graphics, final MapData mapData, final AffineTransform unscaled, final AffineTransform scaled) {
        if (m_overflow) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(m_placementPoint.x - bounds.x - 2, m_placementPoint.y - bounds.y + m_uiContext.getUnitImageFactory().getUnitImageHeight(), m_uiContext.getUnitImageFactory().getUnitImageWidth() + 2, 3);
        }
        final UnitType type = data.getUnitTypeList().getUnitType(m_unitType);
        if (type == null) throw new IllegalStateException("Type not found:" + m_unitType);
        final PlayerID owner = data.getPlayerList().getPlayerID(m_playerName);
        Image img = m_uiContext.getUnitImageFactory().getImage(type, owner, data, m_damaged, m_disabled);
        if ((UnitAttachment.get(type).getIsFactory() || UnitAttachment.get(type).getCanBeDamaged()) && (isDamageFromBombingDoneToUnitsInsteadOfTerritories(data))) {
            if (m_territoryName.length() != 0) {
                final Collection<Unit> units = Match.getMatches(data.getMap().getTerritory(m_territoryName).getUnits().getUnits(), Matches.unitIsOfType(type));
                for (final Unit current : units) {
                    if (Matches.UnitIsDisabled().match(current)) {
                        img = m_uiContext.getUnitImageFactory().getImage(type, owner, data, m_damaged, true);
                    }
                }
            } else {
                img = m_uiContext.getUnitImageFactory().getImage(type, owner, data, m_damaged, m_disabled);
            }
        }
        if (!m_damaged && (UnitAttachment.get(type).getIsFactory() || UnitAttachment.get(type).getCanBeDamaged()) && (isSBRAffectsUnitProduction(data) || isDamageFromBombingDoneToUnitsInsteadOfTerritories(data))) {
            if (m_territoryName.length() != 0) {
                if (isSBRAffectsUnitProduction(data)) {
                    final TerritoryAttachment ta = TerritoryAttachment.get(data.getMap().getTerritory(m_territoryName));
                    final int prod = ta.getProduction();
                    final int unitProd = ta.getUnitProduction();
                    if (unitProd < prod) {
                        img = m_uiContext.getUnitImageFactory().getImage(type, owner, data, true, m_disabled);
                    }
                } else if (isDamageFromBombingDoneToUnitsInsteadOfTerritories(data)) {
                    final Collection<Unit> units = Match.getMatches(data.getMap().getTerritory(m_territoryName).getUnits().getUnits(), Matches.unitIsOfType(type));
                    for (final Unit current : units) {
                        if (Matches.UnitHasSomeUnitDamage().match(current)) {
                            img = m_uiContext.getUnitImageFactory().getImage(type, owner, data, true, m_disabled);
                        }
                    }
                }
            } else {
                img = m_uiContext.getUnitImageFactory().getImage(type, owner, data, m_damaged, m_disabled);
            }
        } else {
            img = m_uiContext.getUnitImageFactory().getImage(type, owner, data, m_damaged, m_disabled);
        }
        graphics.drawImage(img, m_placementPoint.x - bounds.x, m_placementPoint.y - bounds.y, null);
        if (m_count != 1) {
            if (Properties.getCountersDisplay(data) > 0) {
                for (int i = 1; i < m_count && i < Properties.getCountersDisplay(data); i++) {
                    graphics.drawImage(img, m_placementPoint.x + 2 * i - bounds.x, m_placementPoint.y - 2 * i - bounds.y, null);
                }
                if (m_count > Properties.getCountersDisplay(data)) {
                    graphics.setColor(Color.white);
                    graphics.setFont(MapImage.MAP_FONT);
                    graphics.drawString(String.valueOf(m_count), m_placementPoint.x - bounds.x + 2 * Properties.getCountersDisplay(data) + (m_uiContext.getUnitImageFactory().getUnitImageWidth() * 6 / 10), m_placementPoint.y - 2 * Properties.getCountersDisplay(data) - bounds.y + m_uiContext.getUnitImageFactory().getUnitImageHeight() * 1 / 3);
                }
            } else {
                graphics.setColor(Color.white);
                graphics.setFont(MapImage.MAP_FONT);
                graphics.drawString(String.valueOf(m_count), m_placementPoint.x - bounds.x + (m_uiContext.getUnitImageFactory().getUnitImageWidth() / 4), m_placementPoint.y - bounds.y + m_uiContext.getUnitImageFactory().getUnitImageHeight());
            }
        }
        if ((isSBRAffectsUnitProduction(data) || isDamageFromBombingDoneToUnitsInsteadOfTerritories(data)) && (UnitAttachment.get(type).getIsFactory() || UnitAttachment.get(type).getCanBeDamaged())) {
            displayFactoryDamage(bounds, data, graphics, type);
        }
    }

    private void displayFactoryDamage(final Rectangle bounds, final GameData data, final Graphics2D graphics, final UnitType type) {
        graphics.setColor(Color.black);
        graphics.setFont(MapImage.MAP_FONT);
        if (m_territoryName.length() != 0) {
            if (isSBRAffectsUnitProduction(data)) {
                final TerritoryAttachment ta = TerritoryAttachment.get(data.getMap().getTerritory(m_territoryName));
                final int damageCount = ta.getProduction() - ta.getUnitProduction();
                if (damageCount > 0) {
                    graphics.drawString(String.valueOf(damageCount), m_placementPoint.x - bounds.x + (m_uiContext.getUnitImageFactory().getUnitImageWidth() / 4), m_placementPoint.y - bounds.y + m_uiContext.getUnitImageFactory().getUnitImageHeight() / 4);
                }
            } else if (isDamageFromBombingDoneToUnitsInsteadOfTerritories(data)) {
                final Collection<Unit> units = Match.getMatches(data.getMap().getTerritory(m_territoryName).getUnits().getUnits(), Matches.unitIsOfType(type));
                for (final Unit current : units) {
                    final TripleAUnit taUnit = (TripleAUnit) current;
                    if (taUnit.getUnitDamage() > 0) {
                        graphics.drawString(String.valueOf(taUnit.getUnitDamage()), m_placementPoint.x - bounds.x + (m_uiContext.getUnitImageFactory().getUnitImageWidth() / 4), m_placementPoint.y - bounds.y + m_uiContext.getUnitImageFactory().getUnitImageHeight() / 4);
                    }
                }
            }
        }
    }

    public Tuple<Territory, List<Unit>> getUnits(final GameData data) {
        final Territory t = data.getMap().getTerritory(m_territoryName);
        final UnitType type = data.getUnitTypeList().getUnitType(m_unitType);
        final CompositeMatch<Unit> selectedUnits = new CompositeMatchAnd<Unit>();
        selectedUnits.add(Matches.unitIsOfType(type));
        selectedUnits.add(Matches.unitIsOwnedBy(data.getPlayerList().getPlayerID(m_playerName)));
        if (m_damaged) selectedUnits.add(Matches.UnitIsDamaged); else selectedUnits.add(Matches.UnitIsNotDamaged);
        final List<Unit> rVal = t.getUnits().getMatches(selectedUnits);
        return new Tuple<Territory, List<Unit>>(t, rVal);
    }

    public int getLevel() {
        return UNITS_LEVEL;
    }

    @Override
    public String toString() {
        return "UnitsDrawer for " + m_count + " " + MyFormatter.pluralize(m_unitType) + " in  " + m_territoryName;
    }

    private boolean isSBRAffectsUnitProduction(final GameData data) {
        return games.strategy.triplea.Properties.getSBRAffectsUnitProduction(data);
    }

    private boolean isDamageFromBombingDoneToUnitsInsteadOfTerritories(final GameData data) {
        return games.strategy.triplea.Properties.getDamageFromBombingDoneToUnitsInsteadOfTerritories(data);
    }
}
