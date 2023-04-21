package com.budee.crm.web;

import org.apache.commons.fileupload.ProgressListener;
import javax.servlet.http.HttpSession;
import com.budee.crm.vo.FileUploadStatusVO;
import com.budee.crm.utils.Constants;

public class FileUploadListener implements ProgressListener {

    private HttpSession session = null;

    public FileUploadListener(HttpSession session) {
        this.session = session;
    }

    /**
	 * 更新状态
	 * @param pBytesRead 读取字节总数
	 * @param pContentLength 数据总长度
	 * @param pItems 当前正在被读取的field号
	 */
    public void update(long pBytesRead, long pContentLength, int pItems) {
        FileUploadStatusVO fuploadStatus = takeOutFileUploadStatus(this.session);
        if (fuploadStatus == null) return;
        fuploadStatus.setUploadTotalSize(pContentLength);
        if (pContentLength == -1) {
            fuploadStatus.setStatus("completed");
            fuploadStatus.setReadTotalSize(pBytesRead);
            fuploadStatus.setSuccessUploadFileCount(pItems);
            fuploadStatus.setProcessEndTime(System.currentTimeMillis());
            fuploadStatus.setProcessRunningTime(fuploadStatus.getProcessEndTime());
        } else {
            fuploadStatus.setStatus("incompleted");
            fuploadStatus.setReadTotalSize(pBytesRead);
            fuploadStatus.setCurrentUploadFileNum(pItems);
            fuploadStatus.setProcessRunningTime(System.currentTimeMillis());
        }
        storeFileUploadStatus(this.session, fuploadStatus);
    }

    /**
	 * get FileUploadStatusVO from request
	 * @param request
	 * @return
	 */
    public static FileUploadStatusVO takeOutFileUploadStatus(HttpSession session) {
        Object obj = session.getAttribute(Constants.UPLOAD_STATUS);
        if (obj != null) {
            return (FileUploadStatusVO) obj;
        } else {
            return null;
        }
    }

    /**
	 * save FileUploadStatusVO into session
	 * @param request
	 * @param uploadStatusBean
	 */
    public static void storeFileUploadStatus(HttpSession session, FileUploadStatusVO uploadStatus) {
        if (uploadStatus != null) session.setAttribute(Constants.UPLOAD_STATUS, uploadStatus);
    }
}
