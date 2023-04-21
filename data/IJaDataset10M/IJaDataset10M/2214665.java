package com.botarena.client.battle.remote;

import java.util.List;
import com.botarena.shared.BattleInfo;
import com.botarena.shared.BattlePreviewInfo;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("battleAccess")
public interface BattleAccess extends RemoteService {

    public List<BattleInfo> getLatestBattlesInfo(String contestKey, int count);

    public List<BattleInfo> getBattles(int count, String contestKey, String botKey);

    public List<BattleInfo> getBattlesPart(List<String> battleKeys);

    public BattlePreviewInfo getBattleProcess(String battleKey);
}
