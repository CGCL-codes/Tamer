package com.uro.common.egov;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;

/**  
 * @Class Name : ImagePaginationRenderer.java
 * @Description : ImagePaginationRenderer Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 * 
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by MOPAS All right reserved.
 */
public class EgovImgPaginationRenderer extends AbstractPaginationRenderer {

    /**
    * PaginationRenderer
	* 
    * @see 개발프레임웍크 실행환경 개발팀
    */
    public EgovImgPaginationRenderer() {
        String strWebDir = "/###ARTIFACT_ID###/images/egovframework/cmmn/";
        firstPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<image src='" + strWebDir + "btn_page_pre10.gif' border=0/></a>&#160;";
        previousPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<image src='" + strWebDir + "btn_page_pre1.gif' border=0/></a>&#160;";
        currentPageLabel = "<strong>{0}</strong>&#160;";
        otherPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">{2}</a>&#160;";
        nextPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<image src='" + strWebDir + "btn_page_next10.gif' border=0/></a>&#160;";
        lastPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<image src='" + strWebDir + "btn_page_next1.gif' border=0/></a>&#160;";
    }
}
