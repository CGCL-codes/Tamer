package egovframework.com.utl.pao.service;

import java.io.Serializable;

/**
 * 
 * 관인이미지 모델 클래스
 * @author 공통서비스 개발팀 이중호
 * @since 2009.02.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.01  이중호          최초 생성
 *
 * </pre>
 */
public class PrntngOutptVO implements Serializable {

    private byte[] imgInfo;

    private String imgType;

    private String orgCode;

    private String erncslSe;

    /**
	 * imgInfo attribute 를 리턴한다.
	 * @return byte[]
	 */
    public byte[] getImgInfo() {
        return imgInfo;
    }

    /**
	 * imgInfo attribute 값을 설정한다.
	 * @param imgInfo byte[]
	 */
    public void setImgInfo(byte[] imgInfo) {
        this.imgInfo = imgInfo;
    }

    /**
	 * imgType attribute 를 리턴한다.
	 * @return String
	 */
    public String getImgType() {
        return imgType;
    }

    /**
	 * imgType attribute 값을 설정한다.
	 * @param imgType String
	 */
    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    /**
	 * orgCode attribute 를 리턴한다.
	 * @return String
	 */
    public String getOrgCode() {
        return orgCode;
    }

    /**
	 * orgCode attribute 값을 설정한다.
	 * @param orgCode String
	 */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
	 * erncslSe attribute 를 리턴한다.
	 * @return String
	 */
    public String getErncslSe() {
        return erncslSe;
    }

    /**
	 * erncslSe attribute 값을 설정한다.
	 * @param erncslSe String
	 */
    public void setErncslSe(String erncslSe) {
        this.erncslSe = erncslSe;
    }
}
