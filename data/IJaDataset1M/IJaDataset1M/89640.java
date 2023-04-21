package jokeboxjunior.core.playlist;

import java.util.ArrayList;
import jokeboxjunior.core.model.AbstractMediaFile;

/**
 *
 * @author B1
 */
public abstract class AbstractMediaPlaylist extends ArrayList<AbstractMediaFile> implements IMediaPlaylist {

    protected int myCurrentIndex = 0;

    protected boolean myAutoRepeat = true;

    public int addFile(AbstractMediaFile thisFile) {
        super.add(thisFile);
        return super.indexOf(thisFile);
    }

    public boolean hasNext() {
        if (myAutoRepeat) return true;
        if (this.size() == 0) return false;
        return (myCurrentIndex >= this.size() - 1);
    }

    public void next() {
        if (!hasNext()) return;
        if (myAutoRepeat && myCurrentIndex >= this.size() - 1) {
            myCurrentIndex = 0;
        } else {
            myCurrentIndex++;
        }
    }

    public boolean hasPrevious() {
        if (myAutoRepeat) return true;
        if (this.size() == 0) return false;
        return (myCurrentIndex > 0);
    }

    public void previous() {
        if (!hasPrevious()) return;
        if (myAutoRepeat && myCurrentIndex <= 0) {
            myCurrentIndex = this.size() - 1;
        } else {
            myCurrentIndex--;
        }
    }

    @Override
    public void clear() {
        super.clear();
        myCurrentIndex = 0;
    }

    public AbstractMediaFile getCurrentFile() {
        if (this.size() == 0) return null;
        return this.get(myCurrentIndex);
    }

    public int getCurrentIndex() {
        return myCurrentIndex;
    }

    public void setCurrentIndex(int thisIndex) {
        if (thisIndex < 0) thisIndex = 0;
        if (thisIndex > this.size() - 1) thisIndex = this.size() - 1;
        myCurrentIndex = thisIndex;
    }

    public boolean getAutoRepeat() {
        return myAutoRepeat;
    }

    public void setAutoRepeat(boolean thisValue) {
        myAutoRepeat = thisValue;
    }

    @Override
    public String toString() {
        String myReturnStr = "Contents of playlist:\n";
        for (AbstractMediaFile myActFile : this) {
            myReturnStr += myActFile.getFilename() + "\n";
        }
        return myReturnStr;
    }
}
