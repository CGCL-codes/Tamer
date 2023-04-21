package com.google.code.burija.normal.dao;

import java.util.List;
import org.seasar.dao.annotation.tiger.Arguments;
import org.seasar.dao.annotation.tiger.S2Dao;
import com.google.code.burija.normal.dto.PartyInfoDto;

/**
 * 宴会一覧(PARTY_INFO)用Dao。
 * @author imaitakafumi
 *
 */
@S2Dao(bean = PartyInfoDto.class)
public interface PartyInfoDao {

    /**
	 * ログインしているユーザのSYSUSER_IDによる検索。
	 * @param loginUserId - ログインしているユーザのSYSUSER_ID
	 * @return - 宴会一覧
	 */
    @Arguments("loginUserId")
    List<PartyInfoDto> selectList(long loginUserId);

    /**
	 * ログインしているユーザのSYSUSER_ID、PARTY_IDによる検索。
	 * @param loginUserId - ログインしているユーザのSYSUSER_ID
	 * @param partyId - 対象の宴会のPARTY_ID
	 * @return - 宴会一覧の1行
	 */
    @Arguments({ "loginUserId", "partyId" })
    PartyInfoDto selectOne(long loginUserId, long partyId);

    /**
	 * ログインしているユーザのSYSUSER_ID、募集の状態(SOLICIT_STATUS)、宴会の状態(PARTY_STATUS)による検索。
	 * @param loginUserId - ログインしているユーザのSYSUSER_ID
	 * @param solicitStatus - 募集の状態
	 * @param partyStatus - 宴会の状態
	 * @return - 宴会一覧
	 */
    @Arguments({ "loginUserId", "solicitStatus", "partyStatus" })
    List<PartyInfoDto> selectListWithStatus(long loginUserId, int solicitStatus, int partyStatus);
}
