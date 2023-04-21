package com.player;

import ade.ADEServer;
import com.interfaces.*;
import java.rmi.*;

/**
Defines the set of remotely available methods that an {@link ade.ADEServer
ADEServer} must provide to use the Player/Stage position software.

@author Jim Kramer
*/
public interface PlayerPositionServer extends ADEServer, PlayerServer, PositionSensorServer, VelocityServer {
}
