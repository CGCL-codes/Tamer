package com.cell.script;

import java.util.Vector;

/**
 * 

#**************************************************************************
# 脚本为字符流，虚拟机是一个状态机
# 
# '#' 号后面的内容为注释
# ';' 为分隔符
#
# 常量
# _HUMAN				人类	
# _ELF					精灵
# _ORC					兽人
#
#**************************************************************************

#**************************************************************************
# 标签
＃ <标签>
#**************************************************************************
<main> 主入口

<label>


#**************************************************************************
# 变量定义区域，变量定义和使用兼容DOS命令
# 变量定义格式为:
# set <变量名> = <变量值>
# 变量使用格式
# %<变量名>%
#**************************************************************************

set URL = http://www.abc.com;

if( %URL% == http://www.abc.com){
	yes;
}else{
	no;
}

#**************************************************************************
# 判断语句
# if (<value>){ 
# }
# else if (<value>){ 
# }
# value只能取t或f,为t的时候有效
#
#**************************************************************************
if((a==b or a==c) and b!=c)
{
	[jump:LABEL_1];
}
else if(a==c)
{
	[jump:LABEL_2];
}




 */
public class Runner {

    public abstract static class Instruction {

        long Line;

        String Instruct;

        public Instruction(long line, String instruct) {
            Line = line;
            Instruct = instruct;
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof Instruction) {
                return ((Instruction) obj).Line == this.Line;
            }
            return false;
        }

        public abstract void run(Runner runner);
    }

    public static String evaluation(String src) {
        src = src.trim();
        String[] kv = new String[2];
        int index = -1;
        if ((index = src.indexOf("or")) >= 0) {
            kv[0] = evaluation(src.substring(0, index));
            kv[1] = evaluation(src.substring(index + 2));
            if (kv[0].equals("t") || kv[1].equals("t")) {
                src = "t";
            } else {
                src = "f";
            }
        } else if ((index = src.indexOf("and")) >= 0) {
            kv[0] = evaluation(src.substring(0, index));
            kv[1] = evaluation(src.substring(index + 2));
            if (kv[0].equals("t") && kv[1].equals("t")) {
                src = "t";
            } else {
                src = "f";
            }
        } else if ((index = src.indexOf("==")) >= 0) {
            kv[0] = evaluation(src.substring(0, index));
            kv[1] = evaluation(src.substring(index + 2));
            if (kv[0].equals(kv[1])) {
                src = "t";
            } else {
                src = "f";
            }
        } else if ((index = src.indexOf("!=")) >= 0) {
            kv[0] = evaluation(src.substring(0, index));
            kv[1] = evaluation(src.substring(index + 2));
            if (kv[0].equals(kv[1])) {
                src = "f";
            } else {
                src = "t";
            }
        }
        return src;
    }

    final class Lable extends Instruction {

        int Addr;

        public Lable(long line, String instruct) {
            super(line, instruct);
        }

        public void run(Runner runner) {
        }
    }

    final class Jump extends Instruction {

        public Jump(long line, String instruct) {
            super(line, instruct);
        }

        public void run(Runner runner) {
        }
    }

    final class Set extends Instruction {

        public Set(long line, String instruct) {
            super(line, instruct);
        }

        public void run(Runner runner) {
        }
    }

    Vector<Instruction> Instructions;

    int _IP;

    public void update() {
        try {
            Instruction task = Instructions.elementAt(_IP);
            task.run(this);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
