package pcgen.CharacterViewer.resources;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;
import pcgen.android.Logger;

public class GameResourceHistoryItem extends GameResourceEx {

    public GameResourceHistoryItem() {
    }

    public GameResourceHistoryItem(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<GameResourceHistoryItem> CREATOR = new Parcelable.Creator<GameResourceHistoryItem>() {

        public GameResourceHistoryItem createFromParcel(Parcel in) {
            return new GameResourceHistoryItem(in);
        }

        public GameResourceHistoryItem[] newArray(int size) {
            return new GameResourceHistoryItem[size];
        }
    };

    public int getType() {
        return _type;
    }

    public int getTypeSub() {
        return _type_sub;
    }

    public int getValueNew() {
        return _value_new;
    }

    public int getValuePrevious() {
        return _value_previous;
    }

    public JSONObject load(JSONObject data) throws JSONException {
        setType(getJSONInt(data, "type"));
        setTypeSub(getJSONInt(data, "typeSub"));
        setValueNew(getJSONInt(data, "valueNew"));
        setValuePrevious(getJSONInt(data, "valuePrevious"));
        return data;
    }

    public JSONObject save() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("type", getType());
        data.put("typeSub", getTypeSub());
        data.put("valueNew", getValueNew());
        data.put("valuePrevious", getValuePrevious());
        return data;
    }

    public void setType(int value) {
        _type = value;
    }

    public void setTypeSub(int value) {
        _type_sub = value;
    }

    public void setValueNew(int value) {
        _value_new = value;
    }

    public void setValuePrevious(int value) {
        _value_previous = value;
    }

    @Override
    protected void readFromParcelTransform(Parcel in) throws Throwable {
        try {
            setType(in.readInt());
            setTypeSub(in.readInt());
            setValueNew(in.readInt());
            setValuePrevious(in.readInt());
        } catch (Throwable tr) {
            Logger.e(TAG, "readFromParcelTransform", tr);
            throw tr;
        }
    }

    @Override
    protected void writeToParcelTransform(Parcel dest, int flags) throws Throwable {
        try {
            dest.writeInt(getType());
            dest.writeInt(getTypeSub());
            dest.writeInt(getValueNew());
            dest.writeInt(getValuePrevious());
        } catch (Throwable tr) {
            Logger.e(TAG, "writeToParcelTransform", tr);
            throw tr;
        }
    }

    private int _type;

    private int _type_sub;

    private int _value_new;

    private int _value_previous;

    public static final int TYPE_DAMAGE_ABILITY = 1;

    public static final int TYPE_DAMAGE_PRIMARY = 2;

    public static final int TYPE_DAMAGE_SECONDARY = 3;

    public static final int TYPE_INITIATIVE = 4;

    public static final int TYPE_TEMP_ABILITY = 5;

    public static final int TYPE_TEMP_SAVE = 6;

    private static final String TAG = GameResourceHistoryItem.class.getSimpleName();
}
