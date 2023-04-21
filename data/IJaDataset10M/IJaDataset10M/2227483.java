package misc;

import java.io.*;

/**A listener for file changed events
  *
  *@author Tan Hong Cheong
  *2version 20041210
  */
public interface MyFileChangedListener {

    /**@param f The new file being changed
    */
    public void fileChanged(File f);
}
