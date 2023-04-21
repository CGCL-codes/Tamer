package org.nomicron.suber.controller;

import org.nomicron.suber.constants.RequestAttributes;
import org.nomicron.suber.enums.PlayerStatus;
import org.nomicron.suber.model.object.Player;
import org.nomicron.suber.model.bean.PointType;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the player page.
 */
public class PlayerListController extends BaseController {

    /**
     * Create a reference data map for the given request.
     *
     * @param request current HTTP request
     * @return a Map with reference data entries, or null if none
     * @throws Exception in case of invalid state or arguments
     * @see org.springframework.web.servlet.ModelAndView
     */
    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        HashMap model = new HashMap();
        List<PlayerStatus> playerStatusList = PlayerStatus.getDisplayList();
        model.put(RequestAttributes.PLAYER_STATUS_LIST, playerStatusList);
        List<PointType> pointTypeList = getMetaFactory().getPointTypeFactory().getPointTypeList();
        model.put(RequestAttributes.POINT_TYPE_LIST, pointTypeList);
        HashMap<PlayerStatus, Map<Player, Map<PointType, BigDecimal>>> playerStatusMap = new HashMap<PlayerStatus, Map<Player, Map<PointType, BigDecimal>>>();
        for (PlayerStatus playerStatus : playerStatusList) {
            HashMap<Player, Map<PointType, BigDecimal>> playerMap = new HashMap<Player, Map<PointType, BigDecimal>>();
            for (Player player : getMetaFactory().getPlayerFactory().getPlayerListByStatus(playerStatus)) {
                HashMap<PointType, BigDecimal> pointTypeMap = new HashMap<PointType, BigDecimal>();
                for (PointType pointType : pointTypeList) {
                    pointTypeMap.put(pointType, getMetaFactory().getPointEventFactory().getPointTotal(player, pointType).stripTrailingZeros());
                }
                playerMap.put(player, pointTypeMap);
            }
            playerStatusMap.put(playerStatus, playerMap);
        }
        model.put(RequestAttributes.PLAYER_MAP, playerStatusMap);
        return model;
    }
}
