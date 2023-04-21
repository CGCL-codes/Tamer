## Fine-grained Code Clone Detection with Block-based Splitting of Abstract Syntax Tree

In this paper, we design Tamer, a scalable and fine-grained tree-based syntactic code clone detector. Specifically, we propose a novel method to transform the complex abstract syntax tree into simple subtrees. It can accelerate the process of detection and implement the fine-grained analysis of clone pairs to locate the concrete clone parts of the code.

Tamer is mainly comprised of three phases: AST Extraction, Clone candidate filter, and Clone verification.

1. AST Extraction: The purpose of this phase is to apply static analysis(we use javaparser in our tool) to generate the corresponding AST. The input of this phase is a method and the output is an AST.
2. Clone candidate filter: The purpose of this phase is to select the candidate by using AST-based n-grams and inverted index. The input of this phase is the AST presentation of source code and the output is the candidate clone pairs.
3. Clone verification: The purpose of this phase is to determine whether two methods are a true clone. The input of this phase is the candidate clone pairs and the output is to report the detection results.

## Project Structure

Tamer  |-- Func.java 	

This file implements a class of AST including splitting the AST and calculating the similarity of subtrees. 

|-- TaskList.java 

This file implements a multi-thread task

|-- main.java 

## Reproduce

- If you want to reproduce our tool, you first need to install **the maven** environment on your computer, you can follow the tutorial on the [website](https://maven.apache.org/install.html).
- you need to modify the file path in main.java in code line:26, we provide all the datasets we use in [**the data** folder]([Tamer/data at main · MyTamer/Tamer (github.com)](https://github.com/MyTamer/Tamer/tree/main/data)).

```
  public static String filedir = "D:\\AST\\data\\id2sourcecode\\id2sourcecode";
```

- The clone result is kept in a hashmap in our source code, and we will print it on the terminal. The corresponding code line is in main.java, line:314. You can output it to any place you like.

```
   System.out.println(clonePairs);
```

As for the report function we mention in our paper, we add an editable module in our source code for users. Our original report will be very huge and always result in Memory overflow, so you can output the report as you like. The corresponding code line is in Func.java, line 88.

```
- if (final_result >= final_verify_score)
-   output_report();
```

### Use our tool

We provide a more convenient way to use our tool.

We generate a jar package that accepts the input and output of the command-line type, and you can run it by the following command.

```
java -Dfiledir=/root/data/id2sourcecode -Doutputpath=/root/data/result.csv -jar finals-1.0-SNAPSHOT.jar

-Dfiledir refers to the methods set you want to detect

-Doutputpath refers to the file path of the detecting result you want to store
```

Attention: we can not promise the version of this jar package is the newest version and it doesn`t support the report function we mention in our paper. Therefore, we suggest that you run the source code directly if you want to use Tamer better.

## Disclaimers

If you encounter something that does not work as expected, please feel free to open a [GitHub Issue]([Issues · MyTamer/Tamer (github.com)](https://github.com/MyTamer/Tamer/issues)) (not in this repository) with the problem, and we will do our best to resolve it. And the continuous version of our tool can be found in [Tamer/data at main · MyTamer/Tamer (github.com)](https://github.com/MyTamer/Tamer/tree/main/data).
