package org.openuss.paperSubmission;

/**
 * @see org.openuss.paperSubmission.PaperSubmission
 */
public class PaperSubmissionDaoImpl extends org.openuss.paperSubmission.PaperSubmissionDaoBase {

    /**
     * @see org.openuss.paperSubmission.PaperSubmissionDao#toPaperSubmissionInfo(org.openuss.paperSubmission.PaperSubmission, org.openuss.paperSubmission.PaperSubmissionInfo)
     */
    public void toPaperSubmissionInfo(org.openuss.paperSubmission.PaperSubmission sourceEntity, org.openuss.paperSubmission.PaperSubmissionInfo targetVO) {
        targetVO.setId(sourceEntity.getId());
        targetVO.setExamId(sourceEntity.getExam().getId());
        targetVO.setUserId(sourceEntity.getSender().getId());
        targetVO.setDeliverDate(sourceEntity.getDeliverDate());
        targetVO.setComment(sourceEntity.getComment());
    }

    /**
     * @see org.openuss.paperSubmission.PaperSubmissionDao#toPaperSubmissionInfo(org.openuss.paperSubmission.PaperSubmission)
     */
    public org.openuss.paperSubmission.PaperSubmissionInfo toPaperSubmissionInfo(final org.openuss.paperSubmission.PaperSubmission entity) {
        final org.openuss.paperSubmission.PaperSubmissionInfo target = new org.openuss.paperSubmission.PaperSubmissionInfo();
        this.toPaperSubmissionInfo(entity, target);
        return target;
    }

    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private org.openuss.paperSubmission.PaperSubmission loadPaperSubmissionFromPaperSubmissionInfo(org.openuss.paperSubmission.PaperSubmissionInfo paperSubmissionInfo) {
        if (paperSubmissionInfo.getId() != null) {
            return this.load(paperSubmissionInfo.getId());
        } else {
            return PaperSubmission.Factory.newInstance();
        }
    }

    /**
     * @see org.openuss.paperSubmission.PaperSubmissionDao#paperSubmissionInfoToEntity(org.openuss.paperSubmission.PaperSubmissionInfo)
     */
    public org.openuss.paperSubmission.PaperSubmission paperSubmissionInfoToEntity(org.openuss.paperSubmission.PaperSubmissionInfo paperSubmissionInfo) {
        org.openuss.paperSubmission.PaperSubmission entity = this.loadPaperSubmissionFromPaperSubmissionInfo(paperSubmissionInfo);
        this.paperSubmissionInfoToEntity(paperSubmissionInfo, entity, true);
        return entity;
    }

    /**
     * @see org.openuss.paperSubmission.PaperSubmissionDao#paperSubmissionInfoToEntity(org.openuss.paperSubmission.PaperSubmissionInfo, org.openuss.paperSubmission.PaperSubmission)
     */
    public void paperSubmissionInfoToEntity(org.openuss.paperSubmission.PaperSubmissionInfo sourceVO, org.openuss.paperSubmission.PaperSubmission targetEntity, boolean copyIfNull) {
        super.paperSubmissionInfoToEntity(sourceVO, targetEntity, copyIfNull);
    }
}
