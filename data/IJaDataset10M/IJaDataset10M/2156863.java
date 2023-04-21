package server;

import java.sql.*;
import java.util.*;
import java.io.*;
import middleend.*;
import middleend.items.*;

/**
 * @author  mike.leske
 */
public class GameData {

    LinkedList<UserData> RegUser = new LinkedList<UserData>();

    LinkedList<UserData> ActiveUser = new LinkedList<UserData>();

    LinkedList<MapObjectDynamic> dynamicList = new LinkedList<MapObjectDynamic>();
}
