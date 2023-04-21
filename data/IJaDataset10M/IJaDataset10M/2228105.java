package com.hk.svr;

import java.util.List;
import java.util.Map;
import com.hk.bean.CmpPhotoSet;
import com.hk.bean.CmpPhotoSetRef;
import com.hk.bean.CmpPhotoVote;
import com.hk.bean.CompanyPhoto;

public interface CompanyPhotoService {

    void createPhoto(CompanyPhoto photo);

    CompanyPhoto deleteCompanhPhoto(long photoId);

    List<CompanyPhoto> getPhotoListByCompanyId(long companyId, int begin, int size);

    List<CompanyPhoto> getPhotoListByCompanyIdVoteStyle(long companyId, int begin, int size);

    List<CompanyPhoto> getPhotoListByCompanyIdNoLogo(long companyId, String logoPath, int begin, int size);

    Map<Long, CompanyPhoto> getCompanyPhotoMapInId(long companyId, List<Long> idList);

    int countPhotoByCompanyIdNoLogo(long companyId, String logoPath);

    CompanyPhoto getCompanyPhoto(long photoId);

    CompanyPhoto getFirstCompanyPhoto(long companyId);

    void updateName(long photoId, String name);

    void updatePinkflg(long photoId, byte pinkflg);

    void updateVoteCount(long photoId, int add);

    /**
	 * 获取指定图片id的前几张，顺序为投票顺序
	 * 
	 * @param companyId
	 * @param photoId
	 * @param begin
	 * @param size
	 * @return
	 */
    List<CompanyPhoto> getPreCompanyPhotoListByCompanyId(long companyId, long photoId, int begin, int size);

    /**
	 * 获取指定图片id的后几张，顺序为投票顺序
	 * 
	 * @param companyId
	 * @param photoId
	 * @param begin
	 * @param size
	 * @return
	 */
    List<CompanyPhoto> getNextCompanyPhotoListByCompanyId(long companyId, long photoId, int begin, int size);

    List<Long> getPhotoIdListByCompanyIdVoteStyle(long companyId);

    boolean createCmpPhotoVote(CmpPhotoVote cmpPhotoVote);

    int countCmpPhotoVoteByPhotoId(long photoId);

    CmpPhotoVote getCmpPhotoVote(long photoId, long userId);

    void deleteCmpPhotoVote(long photoId, long userId);

    int countCompanyPhotoByCompanyId(long companyId);

    void createCmpPhotoSet(CmpPhotoSet cmpPhotoSet);

    void updateCmpPhotoSet(CmpPhotoSet cmpPhotoSet);

    CmpPhotoSet getCmpPhotoSet(long companyId, long setId);

    /**
	 * 删除图集时，需要把与图集关联的cmpphotosetref删除
	 * 
	 * @param companyId
	 * @param setId
	 *            2010-7-21
	 */
    void deleteCmpPhotoSet(long companyId, long setId);

    /**
	 * 如果已经存在关系数据，将不会再次创建
	 * 
	 * @param cmpPhotoSetRef
	 *            2010-7-22
	 */
    void createCmpPhotoSetRef(CmpPhotoSetRef cmpPhotoSetRef);

    void deleteCmpPhotosetRef(long companyId, long oid);

    CmpPhotoSetRef getCmpPhotoSetRef(long companyId, long oid);

    List<CmpPhotoSetRef> getCmpPhotoSetRefListByCompanyIdAndSetId(long companyId, long setId, int begin, int size);

    void setCmpPhotoSetPicPath(long companyId, long setId, String path);

    List<CmpPhotoSet> getCmpPhotoSetListByCompanyId(long companyId);

    List<CmpPhotoSetRef> getCmpPhotoSetRefListByCompanyIdAndPhotoId(long companyId, long photoId);

    Map<Long, CmpPhotoSet> getCmpPhotoSetMapByCompanyIdAndInId(long companyId, List<Long> idList);
}
