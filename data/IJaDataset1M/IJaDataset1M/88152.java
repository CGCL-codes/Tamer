package com.jsu.judge;

import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.jsu.DAO.ResultDAO;
import com.jsu.DAO.SolutionDAO;
import com.jsu.DAO.StatisticsDAO;
import com.jsu.hibernate.domains.Code;
import com.jsu.hibernate.domains.Contest;
import com.jsu.hibernate.domains.Problem;
import com.jsu.hibernate.domains.Result;
import com.jsu.hibernate.domains.Solution;
import com.jsu.hibernate.domains.Statistics;
import com.jsu.hibernate.domains.User;
import com.jsu.io.FileBuilder;
import com.jsu.io.StreamHandler;
import com.jsu.util.DAOFactory;
import com.jsu.util.HibernateUtil;

/**
 * @author Administrator
 *
 */
public class Judger {

    private static final Logger log = Logger.getLogger(Judger.class);

    public static final int Judging = 0;

    public static final int Accepted = 1;

    public static final int Presentation_Error = 2;

    public static final int Time_Limit_Exceeded = 3;

    public static final int Memory_Limit_Exceeded = 4;

    public static final int Wrong_Answer = 5;

    public static final int Compilation_Error = 6;

    public static final int Runtime_Error = 7;

    public static final int System_Error = 8;

    public static final int NO_TESTCASE = 9;

    public static void judge(String runId, int language) {
        try {
            String compileInfo;
            int result = System_Error;
            String regex = "[a-zA-z]+:(\\w)*\\\\JudgeBase\\\\temp\\\\" + runId + "\\\\";
            SolutionDAO solutionDAO = DAOFactory.getInstance().getSolutionDAO();
            Solution solution = solutionDAO.findById(runId, false);
            User user = solution.getUser();
            Problem problem = solution.getProblem();
            Code code = solution.getCode();
            Contest contest = solution.getContest();
            Integer problemId = problem.getProblemId();
            long timeLimit = problem.getTimeLimit();
            long memoryLimit = problem.getMemoryLimit();
            long javaTimeLimit = problem.getJavaTimeLimit();
            long javaMemoryLimit = problem.getJavaMemoryLimit();
            switch(language) {
                case 0:
                    if (FileBuilder.createGCCFile(runId, code.getCode())) {
                        compileInfo = Compiler.compileGCC(runId);
                        if (!"success".equals(compileInfo)) {
                            compileInfo = compileInfo.replaceAll(regex, "");
                            user.setSubmit(user.getSubmit() + 1);
                            List<Solution> ss = DAOFactory.getInstance().getSolutionDAO().findNotInStatus(user, problem, Accepted);
                            if (ss.isEmpty()) {
                                user.setUnSolved(user.getUnSolved() + 1);
                            }
                            problem.setSubmit(problem.getSubmit() + 1);
                            code.setCompileError(compileInfo);
                            if (contest != null) {
                                StatisticsDAO sDAO = DAOFactory.getInstance().getStatisticsDAO();
                                Statistics statics = sDAO.findByReferrence(contest, problem);
                                statics.setCE(statics.getCE() + 1);
                                statics.setTotal(statics.getTotal() + 1);
                                sDAO.makePersistent(statics);
                                ResultDAO resultDAO = DAOFactory.getInstance().getResultDAO();
                                Result r = resultDAO.findByReferrence(user, contest, problem);
                                long timeTemp = new Date().getTime() - contest.getStartTime().getTime();
                                long timeLenth = timeTemp / 1000 + (long) (r.getWrongs() * 60 * 20);
                                r.setTime(r.getTime() + (int) timeLenth);
                                r.setWrongs(r.getWrongs() + 1);
                                resultDAO.makePersistent(r);
                            }
                            solution.setStatus(Compilation_Error);
                            solution.setUser(user);
                            solution.setProblem(problem);
                            solution.setCode(code);
                            solutionDAO.makePersistent(solution);
                            HibernateUtil.commitTransaction();
                            HibernateUtil.closeSession();
                            return;
                        }
                    }
                    break;
                case 1:
                    if (FileBuilder.createGPPFile(runId, code.getCode())) {
                        compileInfo = Compiler.compileGPP(runId);
                        if (!"success".equals(compileInfo)) {
                            compileInfo = compileInfo.replaceAll(regex, "");
                            user.setSubmit(user.getSubmit() + 1);
                            List<Solution> ss = DAOFactory.getInstance().getSolutionDAO().findNotInStatus(user, problem, Accepted);
                            if (ss.isEmpty()) {
                                user.setUnSolved(user.getUnSolved() + 1);
                            }
                            problem.setSubmit(problem.getSubmit() + 1);
                            code.setCompileError(compileInfo);
                            if (contest != null) {
                                StatisticsDAO sDAO = DAOFactory.getInstance().getStatisticsDAO();
                                Statistics statics = sDAO.findByReferrence(contest, problem);
                                statics.setCE(statics.getCE() + 1);
                                statics.setTotal(statics.getTotal() + 1);
                                sDAO.makePersistent(statics);
                                ResultDAO resultDAO = DAOFactory.getInstance().getResultDAO();
                                Result r = resultDAO.findByReferrence(user, contest, problem);
                                long timeTemp = new Date().getTime() - contest.getStartTime().getTime();
                                long timeLenth = timeTemp / 1000 + (long) (r.getWrongs() * 60 * 20);
                                r.setTime(r.getTime() + (int) timeLenth);
                                r.setWrongs(r.getWrongs() + 1);
                                resultDAO.makePersistent(r);
                            }
                            solution.setStatus(Compilation_Error);
                            solution.setUser(user);
                            solution.setProblem(problem);
                            solution.setCode(code);
                            solutionDAO.makePersistent(solution);
                            HibernateUtil.commitTransaction();
                            HibernateUtil.closeSession();
                            return;
                        }
                    }
                    break;
                case 2:
                    if (FileBuilder.createJavaFile(runId, code.getCode())) {
                        compileInfo = Compiler.compileJava(runId);
                        if (!"success".equals(compileInfo)) {
                            compileInfo = compileInfo.replaceAll(regex, "");
                            user.setSubmit(user.getSubmit() + 1);
                            List<Solution> ss = DAOFactory.getInstance().getSolutionDAO().findNotInStatus(user, problem, Accepted);
                            if (ss.isEmpty()) {
                                user.setUnSolved(user.getUnSolved() + 1);
                            }
                            problem.setSubmit(problem.getSubmit() + 1);
                            code.setCompileError(compileInfo);
                            if (contest != null) {
                                StatisticsDAO sDAO = DAOFactory.getInstance().getStatisticsDAO();
                                Statistics statics = sDAO.findByReferrence(contest, problem);
                                statics.setCE(statics.getCE() + 1);
                                statics.setTotal(statics.getTotal() + 1);
                                sDAO.makePersistent(statics);
                                ResultDAO resultDAO = DAOFactory.getInstance().getResultDAO();
                                Result r = resultDAO.findByReferrence(user, contest, problem);
                                long timeTemp = new Date().getTime() - contest.getStartTime().getTime();
                                long timeLenth = timeTemp / 1000 + (long) (r.getWrongs() * 60 * 20);
                                r.setTime(r.getTime() + (int) timeLenth);
                                r.setWrongs(r.getWrongs() + 1);
                                resultDAO.makePersistent(r);
                            }
                            solution.setStatus(Compilation_Error);
                            solution.setUser(user);
                            solution.setProblem(problem);
                            solution.setCode(code);
                            solutionDAO.makePersistent(solution);
                            HibernateUtil.commitTransaction();
                            HibernateUtil.closeSession();
                            return;
                        }
                    }
                    break;
                case 3:
                    if (FileBuilder.createPascalFile(runId, code.getCode())) {
                        compileInfo = Compiler.compilePascal(runId);
                        if (!"success".equals(compileInfo)) {
                            compileInfo = compileInfo.replaceAll(regex, "");
                            user.setSubmit(user.getSubmit() + 1);
                            List<Solution> ss = DAOFactory.getInstance().getSolutionDAO().findNotInStatus(user, problem, Accepted);
                            if (ss.isEmpty()) {
                                user.setUnSolved(user.getUnSolved() + 1);
                            }
                            problem.setSubmit(problem.getSubmit() + 1);
                            code.setCompileError(compileInfo);
                            if (contest != null) {
                                StatisticsDAO sDAO = DAOFactory.getInstance().getStatisticsDAO();
                                Statistics statics = sDAO.findByReferrence(contest, problem);
                                statics.setCE(statics.getCE() + 1);
                                statics.setTotal(statics.getTotal() + 1);
                                sDAO.makePersistent(statics);
                                ResultDAO resultDAO = DAOFactory.getInstance().getResultDAO();
                                Result r = resultDAO.findByReferrence(user, contest, problem);
                                long timeTemp = new Date().getTime() - contest.getStartTime().getTime();
                                long timeLenth = timeTemp / 1000 + (long) (r.getWrongs() * 60 * 20);
                                r.setTime(r.getTime() + (int) timeLenth);
                                r.setWrongs(r.getWrongs() + 1);
                                resultDAO.makePersistent(r);
                            }
                            solution.setStatus(Compilation_Error);
                            solution.setUser(user);
                            solution.setProblem(problem);
                            solution.setCode(code);
                            solutionDAO.makePersistent(solution);
                            HibernateUtil.commitTransaction();
                            HibernateUtil.closeSession();
                            return;
                        }
                    }
                    break;
                default:
                    break;
            }
            File[] inputs = StreamHandler.getInputFiles(problemId + 999);
            File[] outputs = StreamHandler.getOutputFiles(problemId + 999);
            if (inputs == null || outputs == null || inputs.length == 0 || outputs.length == 0 || inputs.length != outputs.length) {
                user.setSubmit(user.getSubmit() + 1);
                problem.setSubmit(problem.getSubmit() + 1);
                if (contest != null) {
                    StatisticsDAO sDAO = DAOFactory.getInstance().getStatisticsDAO();
                    Statistics statics = sDAO.findByReferrence(contest, problem);
                    statics.setSE(statics.getSE() + 1);
                    statics.setTotal(statics.getTotal() + 1);
                    sDAO.makePersistent(statics);
                }
                solution.setStatus(NO_TESTCASE);
                solution.setUser(user);
                solution.setProblem(problem);
                solutionDAO.makePersistent(solution);
                HibernateUtil.commitTransaction();
                HibernateUtil.closeSession();
                return;
            }
            if (language == 2) {
                for (int i = 0; i < outputs.length; i++) {
                    result = JavaRunner.run(runId, javaTimeLimit, javaMemoryLimit, inputs[i], outputs[i]);
                }
            } else {
                for (int i = 0; i < outputs.length; i++) {
                    result = Runner.run(runId, timeLimit, memoryLimit, inputs[i], outputs[i]);
                }
            }
            if (result == Accepted) {
                user.setSubmit(user.getSubmit() + 1);
                user.setAccept(user.getAccept() + 1);
                List<Solution> ss = DAOFactory.getInstance().getSolutionDAO().findInStatus(user, problem, Accepted);
                if (ss.isEmpty()) {
                    user.setSolved(user.getSolved() + 1);
                    if (user.getUnSolved() > 0) {
                        user.setUnSolved(user.getUnSolved() - 1);
                    }
                }
                problem.setSubmit(problem.getSubmit() + 1);
                problem.setAccept(problem.getAccept() + 1);
                if (contest != null) {
                    StatisticsDAO sDAO = DAOFactory.getInstance().getStatisticsDAO();
                    Statistics statics = sDAO.findByReferrence(contest, problem);
                    statics.setAC(statics.getAC() + 1);
                    statics.setTotal(statics.getTotal() + 1);
                    sDAO.makePersistent(statics);
                    ResultDAO resultDAO = DAOFactory.getInstance().getResultDAO();
                    Result r = resultDAO.findByReferrence(user, contest, problem);
                    long timeLenth = (new Date().getTime() - contest.getStartTime().getTime()) / 1000;
                    r.setAC(1);
                    r.setTime(r.getTime() + (int) timeLenth);
                    resultDAO.makePersistent(r);
                }
                long timeWaste = (language == 2) ? JavaRunner.timeWaste : Runner.timeWaste;
                long memoryWaste = (language == 2) ? JavaRunner.memoryWaste : Runner.memoryWaste;
                solution.setUseTime((int) timeWaste);
                solution.setUseMemory((int) memoryWaste);
                solution.setStatus(result);
                solution.setUser(user);
                solution.setProblem(problem);
                solutionDAO.makePersistent(solution);
                HibernateUtil.commitTransaction();
                HibernateUtil.closeSession();
                return;
            } else {
                user.setSubmit(user.getSubmit() + 1);
                List<Solution> ss = DAOFactory.getInstance().getSolutionDAO().findNotInStatus(user, problem, Accepted);
                if (ss.isEmpty()) {
                    user.setUnSolved(user.getUnSolved() + 1);
                }
                problem.setSubmit(problem.getSubmit() + 1);
                if (contest != null) {
                    StatisticsDAO sDAO = DAOFactory.getInstance().getStatisticsDAO();
                    Statistics statics = sDAO.findByReferrence(contest, problem);
                    switch(result) {
                        case Presentation_Error:
                            statics.setPE(statics.getPE() + 1);
                            break;
                        case Time_Limit_Exceeded:
                            statics.setTLE(statics.getTLE() + 1);
                            break;
                        case Memory_Limit_Exceeded:
                            statics.setMLE(statics.getMLE() + 1);
                            break;
                        case Wrong_Answer:
                            statics.setWA(statics.getWA() + 1);
                            break;
                        case Runtime_Error:
                            statics.setRE(statics.getRE() + 1);
                            break;
                        default:
                            statics.setSE(statics.getSE() + 1);
                            break;
                    }
                    statics.setTotal(statics.getTotal() + 1);
                    sDAO.makePersistent(statics);
                    ResultDAO resultDAO = DAOFactory.getInstance().getResultDAO();
                    Result r = resultDAO.findByReferrence(user, contest, problem);
                    long timeTemp = new Date().getTime() - contest.getStartTime().getTime();
                    long timeLenth = timeTemp / 1000 + (long) (r.getWrongs() * 60 * 20);
                    r.setTime(r.getTime() + (int) timeLenth);
                    r.setWrongs(r.getWrongs() + 1);
                    resultDAO.makePersistent(r);
                }
                long timeWaste = (language == 2) ? JavaRunner.timeWaste : Runner.timeWaste;
                long memoryWaste = (language == 2) ? JavaRunner.memoryWaste : Runner.memoryWaste;
                solution.setUseTime((int) timeWaste);
                solution.setUseMemory((int) memoryWaste);
                solution.setStatus(result);
                solution.setUser(user);
                solution.setProblem(problem);
                solutionDAO.makePersistent(solution);
                HibernateUtil.commitTransaction();
                HibernateUtil.closeSession();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString() + "\n" + e.getMessage());
            SolutionDAO solutionDAO = DAOFactory.getInstance().getSolutionDAO();
            Solution solution = solutionDAO.findById(runId, false);
            User user = solution.getUser();
            Problem problem = solution.getProblem();
            Contest contest = solution.getContest();
            user.setSubmit(user.getSubmit() + 1);
            problem.setSubmit(problem.getSubmit() + 1);
            if (contest != null) {
                StatisticsDAO sDAO = DAOFactory.getInstance().getStatisticsDAO();
                Statistics statics = sDAO.findByReferrence(contest, problem);
                statics.setSE(statics.getSE() + 1);
                statics.setTotal(statics.getTotal() + 1);
                sDAO.makePersistent(statics);
            }
            solution.setStatus(System_Error);
            solution.setUser(user);
            solution.setProblem(problem);
            solutionDAO.makePersistent(solution);
            HibernateUtil.commitTransaction();
            HibernateUtil.closeSession();
            return;
        } finally {
            FileBuilder.deleteFiles(runId);
        }
    }
}
