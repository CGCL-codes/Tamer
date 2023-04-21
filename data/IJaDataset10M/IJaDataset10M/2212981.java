package egovframework.mbl.com.ows.service;

import java.io.Serializable;

/**
 * 개요
 * - 오프라인웹 서비스에 대한 VO 클래스를 정의한다.
 * 
 * 상세내용
 * - 오프라인웹 서비스 정보를 조회하기 위해 필요한 정보를 관리한다.
 * @author 조준형
 * @since 2011.08.22
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.08.22  조준형          최초 생성
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class OfflineWebVO extends OfflineWeb implements Serializable {

    /**
	 * 첫페이지 인덱스
	 */
    private int firstIndex = 1;

    /**
	 * 마지막페이지 인덱스
	 */
    private int lastIndex = 1;

    /**
	 * 현재페이지
	 */
    private int pageIndex = 1;

    /**
	 * 페이지 사이즈
	 */
    private int pageSize = 10;

    /**
	 * 페이지 개수
	 */
    private int pageUnit = 10;

    /**
	 * 페이지당 레코드 개수
	 */
    private int recordCountPerPage = 10;

    /**
	 * 검색조건
	 */
    private String searchCondition = "";

    /**
	 * 검색단어
	 */
    private String searchKeyword = "";

    /**
	 * 검색사용여부
	 */
    private String searchUseYn = "";

    /**
	 * 조회건수
	 */
    private int fetchRow = 0;

    /**
	 * 접근 기기에 따른 조회 페이징 처리 정보
	 */
    private String deviceType = "";

    /**
	 * 조회 건수를 가져온다.
	 * @return int 조회 건수
	 */
    public int getFetchRow() {
        return fetchRow;
    }

    /**
	 * 조회 건수를 저장한다.
	 * 
	 * @param fetchRow 조회건수
	 */
    public void setFetchRow(int fetchRow) {
        this.fetchRow = fetchRow;
    }

    /**
	 * 접속 장비타입을 가져온다.
	 * @return String 장비타입
	 */
    public String getDeviceType() {
        return deviceType;
    }

    /**
	 * 조회 건수를 저장한다.
	 * 
	 * @param deviceType 접속 장비타입
	 */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
	 * 첫페이지 인덱스를 가져온다.
	 * @return int 첫페이지 인덱스
	 */
    public int getFirstIndex() {
        return firstIndex;
    }

    /**
	 * 첫페이지 인덱스를 저장한다.
	 * 
	 * @param firstIndex
	 */
    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    /**
	 * 마지막페이지 인덱스를 가져온다.
	 * @return int 마지막페이지 인덱스
	 */
    public int getLastIndex() {
        return lastIndex;
    }

    /**
	 * 마지막페이지 인덱스를 저장한다.
	 * 
	 * @param lastIndex
	 */
    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    /**
	 * 현재페이지를 가져온다.
	 * @return int 현재페이지
	 */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
	 * 현재페이지를 저장한다.
	 * 
	 * @param pageIndex
	 */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
	 * 페이지 사이즈를 가져온다.
	 * @return int 페이지 사이즈
	 */
    public int getPageSize() {
        return pageSize;
    }

    /**
	 * 페이지 사이즈를 저장한다.
	 * 
	 * @param pageSize
	 */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
	 * 페이지 개수를 가져온다.
	 * @return int 페이지 개수
	 */
    public int getPageUnit() {
        return pageUnit;
    }

    /**
	 * 페이지 개수를 저장한다.
	 * 
	 * @param pageUnit
	 */
    public void setPageUnit(int pageUnit) {
        this.pageUnit = pageUnit;
    }

    /**
	 * 페이지당 레코드 개수를 가져온다.
	 * @return int 페이지당 레코드 개수
	 */
    public int getRecordCountPerPage() {
        return recordCountPerPage;
    }

    /**
	 * 페이지당 레코드 개수를 저장한다.
	 * 
	 * @param recordCountPerPage
	 */
    public void setRecordCountPerPage(int recordCountPerPage) {
        this.recordCountPerPage = recordCountPerPage;
    }

    /**
	 * 검색조건을 가져온다.
	 * @return String 검색조건
	 */
    public String getSearchCondition() {
        return searchCondition;
    }

    /**
	 * 검색조건을 저장한다.
	 * 
	 * @param searchCondition
	 */
    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }

    /**
	 * 검색단어를 가져온다.
	 * @return String 검색단어
	 */
    public String getSearchKeyword() {
        return searchKeyword;
    }

    /**
	 * 검색단어를 저장한다.
	 * 
	 * @param searchKeyword
	 */
    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    /**
	 * 검색사용여부를 가져온다.
	 * @return String 검색사용여부
	 */
    public String getSearchUseYn() {
        return searchUseYn;
    }

    /**
	 * 검색사용여부를 저장한다.
	 * 
	 * @param searchUseYn
	 */
    public void setSearchUseYn(String searchUseYn) {
        this.searchUseYn = searchUseYn;
    }
}
