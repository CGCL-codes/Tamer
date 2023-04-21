package by.bsuir.picasso.client.service;

import by.bsuir.picasso.shared.MapInfo;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("maps")
public interface MapsDataService extends RemoteService {

    MapInfo[] getMapsList();

    Long save(MapInfo mapInfo);

    Boolean delete(Long[] ids);

    MapInfo findOpenMap();

    MapInfo openMap(Long mapId);
}
