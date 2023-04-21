package egovframework.com.sym.bat.service;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 배치쉘스크립트를 실행하는 Quartz Job 클래스를 정의한다.
 * 
 * @author 김진만
 * @see
 * <pre>
 * == 개정이력(Modification Information) ==
 * 
 *   수정일       수정자           수정내용
 *  -------     --------    ---------------------------
 *  2010.08.30   김진만     최초 생성
 * </pre>
 */
public class BatchShellScriptJob implements Job {

    /**
	 * logger
	 */
    private final Logger log = Logger.getLogger(this.getClass());

    /**
     * (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        JobDataMap dataMap = jobContext.getJobDetail().getJobDataMap();
        if (log.isDebugEnabled()) {
            log.debug("job[" + jobContext.getJobDetail().getName() + "] " + "Trigger이름 : " + jobContext.getTrigger().getName());
            log.debug("job[" + jobContext.getJobDetail().getName() + "] " + "BatchOpert이름 : " + dataMap.getString("batchOpertId"));
            log.debug("job[" + jobContext.getJobDetail().getName() + "] " + "BatchProgram이름 : " + dataMap.getString("batchProgrm"));
            log.debug("job[" + jobContext.getJobDetail().getName() + "] " + "Parameter이름 : " + dataMap.getString("paramtr"));
        }
        int result = executeProgram(dataMap.getString("batchProgrm"), dataMap.getString("paramtr"));
        jobContext.setResult(result);
    }

    /**
     * 시스템에서 특정 쉘프로그램을 실행한다.
     * @param batchProgrm 배치실행화일
     * @param paramtr 배치실행화일에 전달될 파라미터
     * @return 배치실행화일리턴값(integer)  
     * @exception Exception
    */
    private int executeProgram(String batchProgrm, String paramtr) {
        int result = 0;
        try {
            Process p = null;
            String cmdStr = batchProgrm + " " + paramtr;
            p = Runtime.getRuntime().exec(cmdStr);
            p.waitFor();
            result = p.exitValue();
            log.debug("배치실행화일 - " + cmdStr + "실행완료, 결과값:" + result);
        } catch (Exception e) {
            log.error("배치스크립트 실행 에러 : " + e.getMessage());
            log.debug(e.getMessage(), e);
        }
        return result;
    }
}
