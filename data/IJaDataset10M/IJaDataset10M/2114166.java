package testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import edu.tjpu.share.po.*;
import edu.tjpu.share.util.Base64Util;

public class TestingClient2 {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 9999;

    private Socket socket;

    OutputStream os;

    InputStream is;

    ObjectOutputStream oow;

    ObjectInputStream ois;

    public void request(BufferedReader br) {
        try {
            socket = new Socket(HOST, PORT);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            oow = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            User user = new User();
            System.out.println("请输入用户名");
            String uname = br.readLine();
            user.setUname(uname);
            System.out.println("输入用户ID");
            int uid = Integer.parseInt(br.readLine());
            user.setUid(uid);
            oow.writeObject("request");
            oow.flush();
            oow.writeObject(user);
            oow.flush();
            List<edu.tjpu.share.po.File> fileList = new ArrayList<edu.tjpu.share.po.File>();
            while (true) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    fileList = (List<edu.tjpu.share.po.File>) obj;
                    System.out.println(fileList);
                    break;
                }
            }
            Iterator<edu.tjpu.share.po.File> fileIterator = fileList.iterator();
            while (fileIterator.hasNext()) {
                edu.tjpu.share.po.File file = new edu.tjpu.share.po.File();
                file = fileIterator.next();
                System.out.println("" + file.getFid() + file.getFurl() + file.getFname());
            }
            oow.writeObject("closeconnection");
            oow.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                oow.close();
                os.close();
                ois.close();
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void upload(BufferedReader br) {
        try {
            socket = new Socket(HOST, PORT);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            oow = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            String flag = "none";
            oow.writeObject("upload");
            oow.flush();
            flag = "upload";
            List<Grade> gradeList = new ArrayList<Grade>();
            List<Major> majorList = new ArrayList<Major>();
            List<Classes> classList = new ArrayList<Classes>();
            List<UserForTransfer> userList = new ArrayList<UserForTransfer>();
            while (true) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    if ("upload".equals(flag)) {
                        gradeList = (List<Grade>) obj;
                        int gradeID = 2;
                        Iterator<Grade> gradeIterator = gradeList.iterator();
                        System.out.println("请选择年级ID");
                        while (gradeIterator.hasNext()) {
                            Grade grade = gradeIterator.next();
                            System.out.println("\t" + grade.getGid() + "\t" + grade.getGname());
                        }
                        gradeID = Integer.parseInt(br.readLine());
                        oow.writeObject(new Integer(gradeID));
                        oow.flush();
                        flag = "gradeIDSend";
                        continue;
                    }
                    if ("gradeIDSend".equals(flag)) {
                        majorList = (List<Major>) obj;
                        int majorID = 2;
                        Iterator<Major> majorIterator = majorList.iterator();
                        System.out.println("请选择专业ID");
                        while (majorIterator.hasNext()) {
                            Major major = majorIterator.next();
                            System.out.println("\t" + major.getMid() + "\t" + major.getMname());
                        }
                        majorID = Integer.parseInt(br.readLine());
                        oow.writeObject(new Integer(majorID));
                        oow.flush();
                        flag = "majoeIDSend";
                        continue;
                    }
                    if ("majoeIDSend".equals(flag)) {
                        classList = (List<Classes>) obj;
                        int classID = 2;
                        Iterator<Classes> classIterator = classList.iterator();
                        System.out.println("请选择班级ID");
                        while (classIterator.hasNext()) {
                            Classes classes = classIterator.next();
                            System.out.println("\t" + classes.getCid() + "\t" + classes.getCname());
                        }
                        classID = Integer.parseInt(br.readLine());
                        oow.writeObject(new Integer(classID));
                        oow.flush();
                        flag = "classIDSend";
                        continue;
                    }
                    if ("classIDSend".equals(flag)) {
                        userList = (List<UserForTransfer>) obj;
                        Iterator<UserForTransfer> userIterator = userList.iterator();
                        System.out.println("请选择用户ID “，” 隔开");
                        while (userIterator.hasNext()) {
                            UserForTransfer user = userIterator.next();
                            System.out.println("\tUID:" + user.getUid() + "\t姓名：" + user.getUname() + "  专业：" + user.getMname() + "  班级：" + user.getCname());
                        }
                        List<Integer> userIDList = new ArrayList<Integer>();
                        String iDs = br.readLine();
                        String[] IDArray = iDs.split(",");
                        for (int i = 0; i < IDArray.length; i++) {
                            userIDList.add(Integer.parseInt(IDArray[i]));
                        }
                        oow.writeObject(userIDList);
                        oow.flush();
                        flag = "userIDSend";
                        List<FileForUpload> fileList = new ArrayList<FileForUpload>();
                        System.out.println("输入要分享的文件的绝对路径");
                        StringBuilder furl = new StringBuilder(br.readLine());
                        System.out.println("请输入分享人ID");
                        int uid = Integer.parseInt(br.readLine());
                        Iterator<Integer> userIDIterator = userIDList.iterator();
                        java.io.File file = new java.io.File(furl.toString());
                        StringBuilder filename = furl.delete(0, furl.lastIndexOf("."));
                        while (userIDIterator.hasNext()) {
                            FileForUpload fileForUpload = new FileForUpload();
                            fileForUpload.setBase64bytes(Base64Util.readFileInBASE64(file));
                            fileForUpload.setUidto(userIDIterator.next());
                            fileForUpload.setUid(uid);
                            fileForUpload.setFname(filename.toString());
                            fileList.add(fileForUpload);
                        }
                        oow.writeObject(fileList);
                        oow.flush();
                        flag = "fileSend";
                        break;
                    }
                }
            }
            oow.writeObject("closeconnection");
            oow.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                oow.close();
                os.close();
                ois.close();
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void success(BufferedReader br) {
        try {
            socket = new Socket(HOST, PORT);
            os = socket.getOutputStream();
            oow = new ObjectOutputStream(os);
            oow.writeObject("success");
            oow.flush();
            List<File> successfileList = new ArrayList<File>();
            System.out.println("输入已读的文件ID “,” 隔开");
            String IDs = br.readLine();
            String[] IDList = IDs.split(",");
            for (int i = 0; i < IDList.length; i++) {
                File file = new File();
                file.setFid(Integer.parseInt(IDList[i]));
                successfileList.add(file);
            }
            oow.writeObject(successfileList);
            oow.flush();
            oow.writeObject("closeconnection");
            oow.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                oow.close();
                os.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void login(BufferedReader br) {
        try {
            socket = new Socket(HOST, PORT);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            oow = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            oow.writeObject("login");
            oow.flush();
            User user = new User();
            System.out.println("输入登录用户名");
            String uname = br.readLine();
            System.out.println("输入登录密码");
            String upasswd = br.readLine();
            user.setUname(uname);
            user.setUpasswd(upasswd);
            oow.writeObject(user);
            oow.flush();
            while (true) {
                Object obj = ois.readObject();
                if (obj instanceof User) {
                    User userBack = (User) obj;
                    System.out.println("登录成功");
                    break;
                } else {
                    System.out.println("登陆失败");
                    break;
                }
            }
            oow.writeObject("closeconnection");
            oow.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                oow.close();
                os.close();
                ois.close();
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateUserInfo(BufferedReader br) {
        try {
            socket = new Socket(HOST, PORT);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            oow = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            oow.writeObject("uerinfoupdate");
            oow.flush();
            User user = new User();
            System.out.println("输入修改后用户名");
            String uname = br.readLine();
            user.setUname(uname);
            System.out.println("输入新头像的绝对路径");
            String avatar = br.readLine();
            java.io.File avatarfile = new java.io.File(avatar);
            user.setUavatar(Base64Util.readFileInBASE64(avatarfile));
            System.out.println("输入要修改的用户的ID");
            int uid = Integer.parseInt(br.readLine());
            user.setUid(uid);
            oow.writeObject(user);
            oow.flush();
            while (true) {
                Object obj = ois.readObject();
                if (obj instanceof User) {
                    User userBack = (User) obj;
                    System.out.println("修改成功");
                    break;
                } else {
                    System.out.println("修改失败");
                    break;
                }
            }
            oow.writeObject("closeconnection");
            oow.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                oow.close();
                os.close();
                ois.close();
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void register(BufferedReader br) {
        try {
            socket = new Socket(HOST, PORT);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            oow = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            String flag = "none";
            oow.writeObject("register");
            oow.flush();
            flag = "register";
            List<Grade> gradeList = new ArrayList<Grade>();
            List<Major> majorList = new ArrayList<Major>();
            List<Classes> classList = new ArrayList<Classes>();
            int gmcid = 0;
            while (true) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    if ("register".equals(flag)) {
                        gradeList = (List<Grade>) obj;
                        int gradeID = 2;
                        Iterator<Grade> gradeIterator = gradeList.iterator();
                        System.out.println("请选择年级ID");
                        while (gradeIterator.hasNext()) {
                            Grade grade = gradeIterator.next();
                            System.out.println("\t" + grade.getGid() + "\t" + grade.getGname());
                        }
                        gradeID = Integer.parseInt(br.readLine());
                        oow.writeObject(new Integer(gradeID));
                        oow.flush();
                        flag = "gradeIDSend";
                        continue;
                    }
                    if ("gradeIDSend".equals(flag)) {
                        majorList = (List<Major>) obj;
                        int majorID = 2;
                        Iterator<Major> majorIterator = majorList.iterator();
                        System.out.println("请选择专业ID");
                        while (majorIterator.hasNext()) {
                            Major major = majorIterator.next();
                            System.out.println("\t" + major.getMid() + "\t" + major.getMname());
                        }
                        majorID = Integer.parseInt(br.readLine());
                        oow.writeObject(new Integer(majorID));
                        oow.flush();
                        flag = "majoeIDSend";
                        continue;
                    }
                    if ("majoeIDSend".equals(flag)) {
                        classList = (List<Classes>) obj;
                        int classID = 2;
                        Iterator<Classes> classIterator = classList.iterator();
                        System.out.println("请选择班级ID");
                        while (classIterator.hasNext()) {
                            Classes classes = classIterator.next();
                            System.out.println("\t" + classes.getCid() + "\t" + classes.getCname());
                        }
                        classID = Integer.parseInt(br.readLine());
                        oow.writeObject(new Integer(classID));
                        oow.flush();
                        flag = "classIDSend";
                        continue;
                    }
                } else if (obj instanceof Integer) {
                    if ("classIDSend".equals(flag)) {
                        gmcid = (Integer) obj;
                        if (gmcid != 0) {
                            User userreg = new User();
                            userreg.setGmcid(gmcid);
                            System.out.println("请输入头像完全路径");
                            String avatar = br.readLine();
                            java.io.File avatarfile = new java.io.File(avatar);
                            userreg.setUavatar(Base64Util.readFileInBASE64(avatarfile));
                            System.out.println("请输入用户名");
                            String uname = br.readLine();
                            userreg.setUname(uname);
                            System.out.println("请输入用户密码");
                            String upasswd = br.readLine();
                            userreg.setUpasswd(upasswd);
                            oow.writeObject(userreg);
                            oow.flush();
                        }
                        continue;
                    }
                } else if (obj instanceof User) {
                    User userBack = (User) obj;
                    System.out.println("注册成功");
                    System.out.println(userBack);
                    break;
                } else {
                    System.out.println("注册失败！");
                    break;
                }
            }
            oow.writeObject("closeconnection");
            oow.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                oow.close();
                os.close();
                ois.close();
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        TestingClient2 tc = new TestingClient2();
        while (true) {
            System.out.println("请选择测试类型（输入代表的数字）");
            System.out.println("\t1.请求文件\t2.上传文件\t3.反馈已读\t4.用户注册\t5.用户修改\t6.用户登录\t7.退出");
            int in = 0;
            try {
                in = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch(in) {
                case 1:
                    tc.request(br);
                    break;
                case 2:
                    tc.upload(br);
                    break;
                case 3:
                    tc.success(br);
                    break;
                case 4:
                    tc.register(br);
                    break;
                case 5:
                    tc.updateUserInfo(br);
                    break;
                case 6:
                    tc.login(br);
                    break;
                case 7:
                    System.exit(0);
                    break;
            }
        }
    }
}
