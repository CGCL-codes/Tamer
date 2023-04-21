package cn.tworen.demou.rtmp;

/**
 * Created on  2006-11-23
 *
 * Title       : NSInfo.java
 * Description : 
 * 
 * @author     : LuJinYi
 * @version    : 1.0
 * @Date       : 2006-11-23
 * History     : 
 * 
 */
public interface NSInfo {

    String NS_CLEAR_SUCCESS = "NetStream.Clear.Success";

    String NS_CLEAR_FAILED = "NetStream.Clear.Failed";

    String NS_PUBLISH_START = "NetStream.Publish.Start";

    String NS_PUBLISH_BADNAME = "NetStream.Publish.BadName";

    String NS_FIALED = "NetStream.Failed";

    String NS_UNPUBLISH_SUCCESS = "NetStream.Unpublish.Success";

    String NS_RECORD_START = "NetStream.Record.Start";

    String NS_RECORD_NOACCESS = "NetStream.Record.NoAccess";

    String NS_RECORD_STOP = "NetStream.Record.Stop";

    String NS_RECORD_FAILED = "NetStream.Record.Failed";

    String NS_PLAY_INSUFFICIENT_BW = "NetStream.Play.InsufficientBW";

    String NS_PLAY_START = "NetStream.Play.Start";

    String NS_PLAY_STREAMNOTFOUND = "NetStream.Play.StreamNotFound";

    String NS_PLAY_STOP = "NetStream.Play.Stop";

    String NS_PLAY_FAILED = "NetStream.Play.Failed";

    String NS_PLAY_RESET = "NetStream.Play.Reset";

    String NS_PLAY_PUBLISHNOTIFY = "NetStream.Play.PublishNotify";

    String NS_PLAY_UNPUBLISHNOTIFY = "NetStream.Play.UnpublishNotify";

    String NS_SEEK_FAILED = "NetStream.Seek.Failed";

    String NS_SEEK_NOTIFY = "NetStream.Seek.Notify";

    String NS_UNPAUSE_NOTIFY = "NetStream.Unpause.Notify";

    String NS_PAUSE_NOTIFY = " NetStream.Pause.Notify";
}
