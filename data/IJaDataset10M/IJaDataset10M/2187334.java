package net.sipvip.server.services.contextjson.inte;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import net.sipvip.server.domains.ExtraPar;

public interface ExtraParServ {

    public ArrayList<ExtraPar> getExtraParServ(String fileStr, String pathinfo) throws FileNotFoundException, UnsupportedEncodingException, IOException;
}
